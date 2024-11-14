package backend.service.impl;

import backend.mapper.MajorMapper;
import backend.mapper.SubjectMapper;
import backend.model.VO.major.*;
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

    @Override
    public List<MajorFirstVO> getFirstMajorByDept(Integer department) {
        try {
            return majorMapper
                    .selectMajor(
                            Major.builder().pid(0).department(department).build(),
                            FieldsGenerator.generateFields(Major.class)
                    )
                    .stream()
                    .map(m -> majorConverter.INSTANCE.MajorToMajorFirstVO(m))
                    .toList();

        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<MajorSecondVO> getSecondMajorByFirst(Integer major) {
        try {

            List<Major> majors = majorMapper.selectMajor(
                    Major.builder().pid(major).build(),
                    FieldsGenerator.generateFields(Major.class)
            );

            return majors.stream()
                    .map(m -> {
                        List<SubMajorSubject> initials = subjectMapper
                                .selectSubjectForeach(StringToList.convert(m.getInitial()));
                        List<SubMajorSubject> interviews = subjectMapper
                                .selectSubjectForeach(StringToList.convert(m.getInterview()));
                        return majorConverter.MajorSubjectToMajorSecondVO(m, initials, interviews);
                    })
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    // Old implementations
    /**
     * 获取专业目录
     *
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

        List<CompletableFuture<List<SubMajorVO>>> futures = new ArrayList<>();

        for (MajorVO majorVO : majorVOList) {
            CompletableFuture<List<SubMajorVO>> future = asyncSubMajors(majorVO);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join(); // 等待所有异步操作完成

        for (int i = 0; i < majorVOList.size(); i++)
            majorVOList.get(i).setSubMajors(futures.get(i).join());

        CompletableFuture.runAsync(() -> {
            redisService.saveDataWithExpiration(redisTemplateString, 5, majorVOList);
        });
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
                    CompletableFuture<List<SubMajorSubject>> future1 = asyncSubMajorInitials(major);
                    CompletableFuture<List<SubMajorSubject>> future2 = asyncSubMajorInterviews(major);

                    CompletableFuture<SubMajorVO> combinedFuture = future1.thenCombine(future2,
                            (initials, interviews) -> majorConverter.MajorToSubMajorVO(major, initials, interviews));

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
            throw new RuntimeException(e.getMessage());
        }

        return CompletableFuture.completedFuture(Collections.emptyList());
    }

//    @Async("taskExecutor")
//    public CompletableFuture<List<SubMajorSubject>> asyncSubMajorInitials(Major major) {
//        try {
//            if (major.getInitial() == null || major.getInitial().isEmpty()) {
//                return CompletableFuture.completedFuture(Collections.emptyList());
//            }
//
//            List<Integer> initialList = Major.initialToList(major.getInitial());
//
//            List<CompletableFuture<List<SubMajorSubject>>> futures = new ArrayList<>();
//
//            for (Integer initialId : initialList) {
//                CompletableFuture<List<SubMajorSubject>> future = CompletableFuture.supplyAsync(() -> {
//                    try {
//                        return subjectMapper.selectSubjectForeach(Collections.singletonList(initialId));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return Collections.emptyList(); // 如果查询失败，返回空列表
//                    }
//                });
//                futures.add(future);
//            }
//
//            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//            allOf.join();
//
//            List<SubMajorSubject> allSubjects = new ArrayList<>();
//            for (CompletableFuture<List<SubMajorSubject>> future : futures) {
//                List<SubMajorSubject> subjects = future.get();
//                if (subjects != null && !subjects.isEmpty()) {
//                    allSubjects.addAll(subjects);
//                }
//            }
//
//            return CompletableFuture.completedFuture(allSubjects);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }
//
//    @Async("taskExecutor")
//    public CompletableFuture<List<SubMajorSubject>> asyncSubMajorInterviews(Major major) {
//        try {
//            if (major.getInterview() == null || major.getInterview().isEmpty()) {
//                return CompletableFuture.completedFuture(Collections.emptyList());
//            }
//
//            List<Integer> interviewList = Major.interviewToList(major.getInterview());
//
//            List<CompletableFuture<SubMajorSubject>> futures = new ArrayList<>();
//
//            for (Integer interviewId : interviewList) {
//                CompletableFuture<SubMajorSubject> future = CompletableFuture.supplyAsync(() -> {
//                    try {
//                        return subjectMapper.selectSubMajorSubject(interviewId);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//                });
//                futures.add(future);
//            }
//
//            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//            allOf.join();
//
//            List<SubMajorSubject> allSubjects = new ArrayList<>();
//            for (CompletableFuture<SubMajorSubject> future : futures) {
//                SubMajorSubject subject = future.get();
//                if (subject != null) {
//                    allSubjects.add(subject);
//                }
//            }
//
//            return CompletableFuture.completedFuture(allSubjects);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//        return CompletableFuture.completedFuture(Collections.emptyList());
//    }

    @Async("taskExecutor")
    public CompletableFuture<List<SubMajorSubject>> asyncSubMajorInitials(Major major) {
        try {
            if (major.getInitial() == null || major.getInitial().isEmpty()) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }

            List<Integer> initialList = Major.initialToList(major.getInitial());

            List<SubMajorSubject> subjects = subjectMapper.selectSubjectForeach(initialList);

            return CompletableFuture.completedFuture(subjects);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<List<SubMajorSubject>> asyncSubMajorInterviews(Major major) {
        try {
            if (major.getInterview() == null || major.getInterview().isEmpty()) {
                return CompletableFuture.completedFuture(Collections.emptyList());
            }

            List<Integer> interviewList = Major.interviewToList(major.getInterview());

            List<SubMajorSubject> subjects = subjectMapper.selectSubjectForeach(interviewList);

            return CompletableFuture.completedFuture(subjects);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
