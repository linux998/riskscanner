package io.riskscanner.base.mapper;

import io.riskscanner.base.domain.RuleInspectionReportMapping;
import io.riskscanner.base.domain.RuleInspectionReportMappingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RuleInspectionReportMappingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    long countByExample(RuleInspectionReportMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int deleteByExample(RuleInspectionReportMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int insert(RuleInspectionReportMapping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int insertSelective(RuleInspectionReportMapping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    List<RuleInspectionReportMapping> selectByExample(RuleInspectionReportMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    RuleInspectionReportMapping selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int updateByExampleSelective(@Param("record") RuleInspectionReportMapping record, @Param("example") RuleInspectionReportMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int updateByExample(@Param("record") RuleInspectionReportMapping record, @Param("example") RuleInspectionReportMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int updateByPrimaryKeySelective(RuleInspectionReportMapping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_inspection_report_mapping
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int updateByPrimaryKey(RuleInspectionReportMapping record);
}