package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.mapper.EmployeeMapper;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.types.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(savedEmployee);
    }

    public EmployeeDTO getEmployeeById(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return employeeMapper.toDTO(employee);
    }

    public void setEmployeeAvailability(Set<DayOfWeek> dayOfWeekSet, long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(dayOfWeekSet);
        employeeRepository.save(employee);
    }

    public List<EmployeeDTO> findAvailableEmployees(EmployeeRequestDTO employeeRequestDTO) {
        Set<EmployeeSkill> requestedSkills = employeeRequestDTO.getSkills();
        LocalDate requestedDate = employeeRequestDTO.getDate();

        List<Employee> availableEmployees = employeeRepository.findAll().stream()
                .filter(employee -> employee.getSkills().containsAll(requestedSkills))
                .filter(employee -> employee.getDaysAvailable().contains(requestedDate.getDayOfWeek()))
                .collect(Collectors.toList());

        return availableEmployees.stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }
}

