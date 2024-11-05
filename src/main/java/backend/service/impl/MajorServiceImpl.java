package backend.service.impl;

import backend.mapper.MajorMapper;
import backend.mapper.SubjectMapper;
import backend.model.DTO.MajorDTO;
import backend.model.VO.major.MajorGrabVO;
import backend.model.VO.major.MajorVO;
import backend.model.VO.major.SubMajorVO;
import backend.model.converter.MajorConverter;
import backend.model.entity.Major;
import backend.model.entity.Subject;
import backend.service.MajorService;
import backend.util.FieldsGenerator;
import ch.qos.logback.core.joran.conditional.ElseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private MajorConverter majorConverter;

    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public MajorGrabVO grabMajors(Integer department) {
        List<Major> majorList = majorMapper.selectMajor(Major.builder().pid(0).department(department).build(),
                FieldsGenerator.generateFields(Major.class));
        List<MajorVO> majorVOList = majorConverter.MajorListToMajorVOList(majorList);

        List<CompletableFuture<List<SubMajorVO>>> futures = new ArrayList<>();

        for (MajorVO majorVO : majorVOList) {
            CompletableFuture<List<SubMajorVO>> future = asyncSubMajors(majorVO);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join(); // 等待所有异步操作完成

        for (int i = 0; i < majorVOList.size(); i++)
            majorVOList.get(i).setSubMajors(futures.get(i).join());

        return MajorGrabVO.builder().majors(majorVOList).build();
        // return null;
    }

    @Async("taskExecutor")
    public CompletableFuture<List<SubMajorVO>> asyncSubMajors(MajorVO majorVO) {
        try {
            List<Major> subMajorList = majorMapper.selectMajor(Major.builder().pid(majorVO.getId()).build(),
                    FieldsGenerator.generateFields(Major.class));

            if (!subMajorList.isEmpty()) {
                List<CompletableFuture<SubMajorVO>> futures = new ArrayList<>();

                for (Major major : subMajorList) {
                    CompletableFuture<List<Map<String, String>>> future1 = asyncSubMajorInitials(major);
                    CompletableFuture<List<String>> future2 = asyncSubMajorInterviews(major);

                    CompletableFuture<SubMajorVO> combinedFuture = future1.thenCombine(future2,
                            (initials, interviews) -> {
                                return majorConverter.MajorToSubMajorVO(major, initials, interviews);
                            });

                    futures.add(combinedFuture);
                }

                List<SubMajorVO> subMajorVOList = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                        .thenApply(v -> {
                            List<SubMajorVO> resultList = new ArrayList<>();
                            for (CompletableFuture<SubMajorVO> future : futures) {
                                resultList.add(future.join());
                            }
                            return resultList;
                        }).join();

                return CompletableFuture.completedFuture(subMajorVOList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(Collections.emptyList());

    }

    @Async("taskExecutor")
    public CompletableFuture<List<Map<String, String>>> asyncSubMajorInitials(Major major) {
        try {
            List<CompletableFuture<Map<String, String>>> futures = new ArrayList<>();
            List<Map<String, String>> initials = new ArrayList<>();
            MajorDTO majorDTO = majorConverter.MajorToMajorDTO(major);

            for (Integer initialId : majorDTO.getInitial()) {
                CompletableFuture<Map<String, String>> future = CompletableFuture.supplyAsync(() -> {
                    List<Subject> subjects = subjectMapper.selectSubject(Subject.builder().id(initialId).build(),
                            FieldsGenerator.generateFields(Subject.class));
                    Map<String, String> initialMap = new HashMap<>();

                    if (!subjects.isEmpty()) {
                        Subject subject = subjects.getFirst();
                        initialMap.put(subject.getSid(), subject.getName());
                    }
                    return initialMap;
                });

                futures.add(future);
            }

            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenAccept(v -> {

                        for (CompletableFuture<Map<String, String>> future : futures) {
                            try {
                                initials.add(future.get());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

            allOf.join();

            return CompletableFuture.completedFuture(initials);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

    }

    @Async("taskExecutor")
    public CompletableFuture<List<String>> asyncSubMajorInterviews(Major major) {
        try {

            List<CompletableFuture<String>> futures = new ArrayList<>();
            List<String> initials = new ArrayList<>();
            MajorDTO majorDTO = majorConverter.MajorToMajorDTO(major);

            for (Integer initialId : majorDTO.getInitial()) {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    List<Subject> subjects = subjectMapper.selectSubject(Subject.builder().id(initialId).build(),
                            FieldsGenerator.generateFields(Subject.class));
                    return subjects.isEmpty() ? "" : subjects.getFirst().getName();
                });

                futures.add(future);
            }

            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenAccept(v -> {
                        for (CompletableFuture<String> future : futures) {
                            try {
                                initials.add(future.get());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

            allOf.join();

            return CompletableFuture.completedFuture(initials);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
