package io.riskscanner.base.mapper;

import io.riskscanner.base.domain.RuleType;
import io.riskscanner.base.domain.RuleTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RuleTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    long countByExample(RuleTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int deleteByExample(RuleTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int insert(RuleType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int insertSelective(RuleType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    List<RuleType> selectByExample(RuleTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    RuleType selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int updateByExampleSelective(@Param("record") RuleType record, @Param("example") RuleTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int updateByExample(@Param("record") RuleType record, @Param("example") RuleTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int updateByPrimaryKeySelective(RuleType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rule_type
     *
     * @mbg.generated Tue Jan 19 17:40:09 CST 2021
     */
    int updateByPrimaryKey(RuleType record);
}