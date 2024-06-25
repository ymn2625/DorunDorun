package com.example.dorundorun.entity;

import com.example.dorundorun.dto.RunningRecordDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "runningRecorde")
public class RunningRecordEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long runningRecordId;

    @Column
    private Integer runningTimeHH;

    @Column
    private Integer runningTimeMM;

    @Column
    private Integer runningTimeSS;

    @Column
    private Integer runningTimeTenMillis;

    @Column
    private Integer runningDistanceKm;

    @Column
    private Integer runningDistanceM;

    @Column
    private String runningPace;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    public static RunningRecordEntity toSaveRunningRecordEntity(RunningRecordDTO runningRecordDTO) {
        RunningRecordEntity runningRecordEntity = new RunningRecordEntity();
        runningRecordEntity.setRunningTimeHH(runningRecordDTO.getRunningTimeHH());
        runningRecordEntity.setRunningTimeMM(runningRecordDTO.getRunningTimeMM());
        runningRecordEntity.setRunningTimeSS(runningRecordDTO.getRunningTimeSS());
        runningRecordEntity.setRunningTimeTenMillis(runningRecordDTO.getRunningTimeTenMillis());
        runningRecordEntity.setRunningDistanceKm(runningRecordDTO.getRunningDistanceKm());
        runningRecordEntity.setRunningDistanceM(runningRecordDTO.getRunningDistanceM());

        return runningRecordEntity;
    }
}
