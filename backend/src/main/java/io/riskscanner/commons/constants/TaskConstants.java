package io.riskscanner.commons.constants;

/**
 * @author maguohao
 */
public class TaskConstants {

    public enum TASK_STATUS {
        DRAFT, UNCHECKED, APPROVED, FINISHED, TERMINATED, CANCELED, REJECTED, PROCESSING, ERROR, WARNING, RUNNING, PENDING, PAUSE
    }

    public static final String TASK_ID_PREFIX = "cs";

    public enum Type {
        CREATE, UPDATE, DELETE, SERVER_EXPIRE_UPDATE, DISK_UPDATE
    }

    public enum MODEL_ID {
        cs_create
    }

    public enum RESULT_TYPE {
        json, yaml, string
    }

    /**
     * 风险等级:
     * 正常
     * 低风险
     * 中风险
     * 高风险
     */
    public enum Severity {
        Normal, LowRisk, MediumRisk, HighRisk
    }

    public enum TaskType {
        manual, quartz
    }

    public final static String CUSTODIAN_RUN_RESULT_FILE = "custodian-run.log";
    public final static String METADATA_RESULT_FILE = "metadata.json";
    public final static String RESOURCES_RESULT_FILE = "resources.json";

    public final static String RESULT_FILE_PATH = "/tmp/{task_id}/policy.yml";
    public final static String RESULT_FILE_PATH_PREFIX = "/tmp/";
}