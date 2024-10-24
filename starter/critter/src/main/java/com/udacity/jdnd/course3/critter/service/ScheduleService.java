package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.mapper.ScheduleMapper;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleMapper scheduleMapper;

    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleMapper.toEntity(scheduleDTO);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(savedSchedule);
    }

    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getSchedulesForPet(long petId) {
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getPets().stream()
                        .anyMatch(pet -> pet.getId() == petId))
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getSchedulesForEmployee(long employeeId) {
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getEmployees().stream()
                        .anyMatch(employee -> employee.getId() == employeeId))
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getSchedulesForCustomer(long customerId) {
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getPets().stream()
                        .anyMatch(pet -> pet.getCustomer().getId() == customerId))
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }
}
