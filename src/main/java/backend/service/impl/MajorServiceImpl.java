package backend.service.impl;

import backend.mapper.MajorMapper;
import backend.mapper.SubjectMapper;
import backend.model.VO.major.*;
import backend.model.VO.subject.SubjectVO;
import backend.model.converter.MajorConverter;
import backend.model.entity.Major;
import backend.redis.RedisService;
import backend.service.MajorService;
import backend.util.FieldsGenerator;
import backend.util.StringToList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private MajorConverter majorConverter;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 获取对应学院下的一级学科
     * /unauthorized/catalogue/second
     * @param department 学院
     * @return 一级学科列表
     */
    @Override
    public List<MajorFirstVO> getFirstMajorByDept(Integer department) {
//        String redisTemplateString = "majorFirst:" + department;
//
//        List<MajorFirstVO> majorVOs = (List<MajorFirstVO>) redisService.getData(redisTemplateString);
//
//        if(majorVOs != null) return majorVOs;

        try {
            List<Major> majorList = majorMapper.selectMajor(
                    Major.builder().pid(0).department(department).build(),
                    FieldsGenerator.generateFields(Major.class)
            );

            List<CompletableFuture<MajorFirstVO>> majorFirstVOListFuture = new ArrayList<>();
            for(Major major: majorList){
                CompletableFuture<MajorFirstVO> majorFirstVOFuture = CompletableFuture.supplyAsync(() ->
                        majorConverter.INSTANCE.MajorToMajorFirstVO(major)
                );
                majorFirstVOListFuture.add(majorFirstVOFuture);
            }


            CompletableFuture.allOf(majorFirstVOListFuture.toArray(new CompletableFuture[0])).join();

            List<MajorFirstVO> majorFirstVOList = majorFirstVOListFuture.stream()
                    .map(CompletableFuture::join)
                    .toList();
//            CompletableFuture.runAsync(() -> redisService.saveDataWithExpiration(redisTemplateString, 30, majorFirstVOList));

            return majorFirstVOList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取对应一级学科下的二级学科
     * GET /unauthorized/catalogue/second
     * @param major 一级学科
     * @return 二级学科列表
     */
    @Override
    public List<MajorSecondVO> getSecondMajorByFirst(Integer major) {
        try {

            List<Major> majors = majorMapper.selectMajor(
                    Major.builder().pid(major).build(),
                    FieldsGenerator.generateFields(Major.class)
            );

            List<CompletableFuture<MajorSecondVO>> majorSecondVOListFuture = new ArrayList<>();

            for(Major m: majors){
                CompletableFuture<List<SubjectVO>> initialsFuture = CompletableFuture.supplyAsync(() ->
                        subjectMapper.selectSubjectForeach(StringToList.convert(m.getInitial()))
                );

                CompletableFuture<List<SubjectVO>> interviewsFuture = CompletableFuture.supplyAsync(() ->
                        subjectMapper.selectSubjectForeach(StringToList.convert(m.getInterview()))
                );

                CompletableFuture<MajorSecondVO> majorSecondVOFuture = initialsFuture.thenCombine(interviewsFuture,
                        (initials, interviews) -> majorConverter.INSTANCE.MajorSubjectToMajorSecondVO(m, initials, interviews));

                majorSecondVOListFuture.add(majorSecondVOFuture);
            }

            CompletableFuture.allOf(majorSecondVOListFuture.toArray(new CompletableFuture[0])).join();

            return majorSecondVOListFuture.stream()
                    .map(CompletableFuture::join)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 获取招生目录
     * GET /unauthorized/catalogue
     * @param department 学院
     * @return 专业列表
     */
    @Override
    public List<MajorVO> getCatalogue(Integer department) {
        String redisTemplateString = "majorCatalogue:" + department;
        List<MajorVO> majorVOs = (List<MajorVO>) redisService.getData(redisTemplateString);

        if(majorVOs != null) return majorVOs;

        List<Major> majorList = majorMapper.selectMajor(Major.builder().pid(0).department(department).build(),
                FieldsGenerator.generateFields(Major.class));
        List<MajorVO> majorVOList = majorConverter.MajorListToMajorVOList(majorList);

        List<CompletableFuture<CompletableFuture<List<SubMajorVO>>>> futures = new ArrayList<>();

        for (MajorVO majorVO : majorVOList) {
            CompletableFuture<CompletableFuture<List<SubMajorVO>>> future =
                    CompletableFuture.supplyAsync(() -> asyncSubMajors(majorVO)).thenApply(v -> v);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        for (int i = 0; i < majorVOList.size(); i++)
            majorVOList.get(i).setSubMajors(futures.get(i).join().join());

        CompletableFuture.runAsync(() -> redisService.saveDataWithExpiration(redisTemplateString, 30, majorVOList));
        return majorVOList;
    }

    @Async("taskExecutor")
    public CompletableFuture<List<SubMajorVO>> asyncSubMajors(MajorVO majorVO) {
        try {
            List<Major> subMajorList = majorMapper.selectMajor(Major.builder().pid(majorVO.getMajorID()).build(),
                    FieldsGenerator.generateFields(Major.class));

            if (!subMajorList.isEmpty()) {
                List<CompletableFuture<SubMajorVO>> futures = new ArrayList<>();

                for (Major major : subMajorList) {

                    CompletableFuture<CompletableFuture<List<SubjectVO>>> future1 =
                            CompletableFuture.supplyAsync(() -> asyncSubMajorInitials(major)).thenApply(v -> v);

                    CompletableFuture<CompletableFuture<List<SubjectVO>>> future2 =
                            CompletableFuture.supplyAsync(() -> asyncSubMajorInterviews(major)).thenApply(v -> v);

                    CompletableFuture<SubMajorVO> combinedFuture = future1.thenCompose(innerFuture1 ->
                            future2.thenCompose(innerFuture2 ->
                                    innerFuture1.thenCombine(innerFuture2,
                                            (initials, interviews) -> majorConverter.MajorToSubMajorVO(major, initials, interviews))));

                    futures.add(combinedFuture);
                }

                return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                        .thenApply(v -> {
                            List<SubMajorVO> resultList = new ArrayList<>();
                            for (CompletableFuture<SubMajorVO> future : futures) {
                                resultList.add(future.join());
                            }
                            return resultList;
                        });
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    @Async("taskExecutor")
    public CompletableFuture<List<SubjectVO>> asyncSubMajorInitials(Major major) {
        try {
            if (major.getInitial() == null || major.getInitial().isEmpty()) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }

            List<Integer> initialList = Major.initialToList(major.getInitial());

            List<SubjectVO> subjects = subjectMapper.selectSubjectForeach(initialList);

            return CompletableFuture.completedFuture(subjects).thenApply(v -> subjects);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<List<SubjectVO>> asyncSubMajorInterviews(Major major) {
        try {
            if (major.getInterview() == null || major.getInterview().isEmpty()) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }

            List<Integer> interviewList = Major.interviewToList(major.getInterview());

            List<SubjectVO> subjects = subjectMapper.selectSubjectForeach(interviewList);

            return CompletableFuture.completedFuture(subjects).thenApply(v -> subjects);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
