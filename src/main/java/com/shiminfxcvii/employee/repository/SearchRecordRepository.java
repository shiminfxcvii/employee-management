package com.shiminfxcvii.employee.repository;

import com.shiminfxcvii.employee.entity.SearchRecord;
import com.shiminfxcvii.employee.service.impl.SearchRecordServiceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;

/**
 * 搜索记录工厂接口，查询和删除搜索记录，用于前台搜索框 autocomplete
 *
 * @author ShiminFXCVII
 * @since 2022/4/24 0:23 周日
 */
@Repository
public interface SearchRecordRepository extends JpaRepository<SearchRecord, Long> {

    /**
     * 查找符合条件的搜索名 %? or %?%<br>
     * 也可不要这个方法，改为在 controller 中使用 findAll()
     *
     * @param username      登陆用户名
     * @param searchGroupBy 搜索字段
     * @param recordName    搜索名称
     * @return 返回符合条件的搜索名 record_name
     * @author ShiminFXCVII
     * @see SearchRecordServiceImpl#getAllPropertiesOfEmployeesBy
     * @since 2022/5/20 14:53
     */
    @Query(value = """
            SELECT record_name
            FROM (
                SELECT record_name, MAX(created_date) mcd FROM employee_management.search_record
                WHERE username = ?1 AND search_group_by = ?2 AND IF('' != ?3, record_name LIKE ?3, TRUE)
                GROUP BY record_name
            ) AS rncd
            ORDER BY mcd DESC LIMIT 0, 10;
            """, nativeQuery = true)
    LinkedHashSet<String> findThisRecordNames(String username,
                                              String searchGroupBy,
                                              String recordName);

}