package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "Employee controller api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */

    @ApiOperation(value = "Employee Login")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */

    @ApiOperation(value = "Employee logout")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


//    新增员工
    @PostMapping
    @ApiOperation(value = "Add Employee") // connected with API document
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("new employee info: {}", employeeDTO);
        System.out.println("current thread ID: " + Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return null;
    }

    @GetMapping("/page")
    @ApiOperation("query employee")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("Employee Query Parameter: {}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Active or not active")
    public Result startOrStop(@PathVariable Integer status, long id) {
        log.info("Active or not active account: {}, {}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("find employee based on id")
    public Result<Employee> getById(@PathVariable long id) {
        Employee employee = employeeService.getById(id);

        return Result.success(employee);
    }

    //DTO -- Json数据 -- use RequestBody
    @PutMapping
    @ApiOperation("Revise employee info")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("Edit employee info: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success(employeeDTO);
    }

}
