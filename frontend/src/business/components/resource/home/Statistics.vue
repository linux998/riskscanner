<template>
    <main-container v-loading="result.loading">

      <el-card class="table-card" v-if="groups.length > 0">
        <el-tabs class="border-card" type="border-card" @tab-click="handleClick">
          <el-tab-pane
            v-for="group in groups"
            :key="group.id"
            :value="group.id"
            :label="group.name">
            <el-row>
              <el-col :span="4">
                <span style="color: #909090;">{{ $t('resource.scene_name') }}</span>
              </el-col>
              <el-col :span="8">
                <span>{{ group.name }}</span>
              </el-col>
              <el-col :span="4">
                <span style="color: #909090;">{{ $t('resource.activation_time') }}</span>
              </el-col>
              <el-col :span="8">
                <span>{{ group.createTime | timestampFormatDate }}</span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="4">
                <span style="color: #909090;">{{ $t('resource.scene_description') }}</span>
              </el-col>
              <el-col :span="8">
                <span>{{ group.description }}</span>
              </el-col>
              <el-col :span="4">
                <span style="color: #909090;">{{ $t('resource.statistics') }}</span>
              </el-col>
              <el-col :span="8">
                <span v-if="group.status == 'risky'" style="color: red;cursor: pointer;" @click="download"><i class="el-icon-download"></i> {{ $t('resource.download_report') }}</span>
                <span v-if="group.status == 'risk_free'" style="color: green;"> {{ group.state }}</span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="4">
                <span style="color: #909090;">{{ $t('resource.equal_guarantee_level') }}</span>
              </el-col>
              <el-col :span="8">
                <span>{{ group.level }}</span>
              </el-col>
              <el-col :span="4">
                <span style="color: #909090;">{{ $t('resource.compliance_results') }}</span>
              </el-col>
              <el-col :span="8">
                <span v-if="group.status == 'risky'" style="color: red;"><i class="el-icon-warning"></i> {{ $t('resource.discover_risk') }}</span>
                <span v-if="group.status == 'risk_free'" style="color: green;"><i class="el-icon-warning"></i> {{ $t('resource.no_risk') }}</span>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-card>

      <el-card class="table-report-card">
        <section class="report-container">
          <main>
            <metric-chart :content="content"/>
          </main>
        </section>
      </el-card>

      <el-card>
        <template v-slot:header>
          <table-header :condition.sync="condition"
                        @search="search"
                        :title="$t('resource.regulation_list')"/>
        </template>


        <el-table border :data="tableData" class="adjust-table table-content" @sort-change="sort" @filter-change="filter" :row-class-name="tableRowClassName">
          <el-table-column type="index" min-width="5%"/>
          <el-table-column prop="itemSortFirstLevel" :label="$t('resource.security_level')" min-width="10%" show-overflow-tooltip></el-table-column>
          <el-table-column prop="itemSortSecondLevel" :label="$t('resource.control_point')" min-width="10%" show-overflow-tooltip></el-table-column>
          <el-table-column prop="project" :label="$t('resource.basic_requirements_for_grade_protection')" min-width="47%" show-overflow-tooltip></el-table-column>
          <el-table-column :label="$t('resource.pre_check_results')" min-width="10%" show-overflow-tooltip>
            <template v-slot:default="scope">
              <el-tooltip class="item" effect="dark" :content="$t('resource.risk_of_non_compliance')" placement="top">
                <span v-if="scope.row.status === 'risky'" style="color: red;">
                    {{ $t('resource.' + scope.row.status) }} <i class="el-icon-warning"></i>
                </span>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" :content="$t('resource.requirements_of_the_regulations')" placement="top">
                <span v-if="scope.row.status === 'risk_free'" style="color: #00bb00;">
                    {{ $t('resource.' + scope.row.status) }} <i class="el-icon-warning"></i>
                </span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column v-slot:default="scope" :label="$t('resource.suggestions_for_improvement')" min-width="10%">
            <el-tooltip v-if="scope.row.status === 'risky'" class="item" effect="dark" :content="scope.row.improvement" placement="top">
              <span style="color: #0066ac;">
                {{ $t('resource.suggestions_for_improvement') }} <i class="el-icon-question"></i>
              </span>
            </el-tooltip>
            <span v-if="scope.row.status === 'risk_free'">
              <i class="el-icon-minus"></i>
            </span>
          </el-table-column>
          <el-table-column min-width="8%" :label="$t('commons.operating')">
            <template v-slot:default="scope">
              <table-operators :buttons="buttons" :row="scope.row"/>
            </template>
          </el-table-column>
        </el-table>
        <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>
      </el-card>


      <!--Rule detail-->
      <el-drawer class="rtl" :title="$t('resource.report_detail')" :visible.sync="visible" size="60%" :before-close="handleClose" :direction="direction"
                 :destroy-on-close="true">
          <el-row class="el-row-c">
            <el-col :span="8"><span style="color: #909090;">{{ $t('resource.basic_requirements_for_grade_protection') }}</span></el-col>
            <el-col :span="16"><span>{{ detailForm.project }}</span></el-col>
          </el-row>
          <el-row class="el-row-c">
            <el-col :span="8"><span style="color: #909090;">{{ $t('resource.security_level') }}</span></el-col>
            <el-col :span="16"><span>{{ detailForm.itemSortFirstLevel }}</span></el-col>
          </el-row>
          <el-row class="el-row-c">
            <el-col :span="8"><span style="color: #909090;">{{ $t('resource.control_point') }}</span></el-col>
            <el-col :span="16"><span>{{ detailForm.itemSortSecondLevel }}</span></el-col>
          </el-row>
          <el-row class="el-row-c">
            <el-col :span="8"><span style="color: #909090;">{{ $t('resource.pre_check_results') }}</span></el-col>
            <el-col :span="16"><span>
              <el-tooltip class="item" effect="dark" :content="$t('resource.risk_of_non_compliance')" placement="top">
                  <span v-if="detailForm.status === 'risky'" style="color: red;">
                      {{ $t('resource.' + detailForm.status) }} <i class="el-icon-warning"></i>
                  </span>
                </el-tooltip>
                <el-tooltip class="item" effect="dark" :content="$t('resource.requirements_of_the_regulations')" placement="top">
                  <span v-if="detailForm.status === 'risk_free'" style="color: #00bb00;">
                      {{ $t('resource.' + detailForm.status) }} <i class="el-icon-warning"></i>
                  </span>
                </el-tooltip>
            </span></el-col>
          </el-row>
          <el-row class="el-row-c">
            <el-col :span="8"><span style="color: #909090;">{{ $t('resource.i18n_detail') }} <i class="el-icon-question"></i></span></el-col>
            <el-col :span="16">
              <span>
                <el-table :data="detailForm.taskList" style="width: 100%">
                    <el-table-column :label="$t('rule.rule_name')" min-width="75%">
                      <template slot-scope="scope">
                        <span v-if="!!scope.row.returnSum && scope.row.returnSum>0"><i class="el-icon-warning-outline" style="color: red"></i> {{ scope.row.taskName }}</span>
                        <span v-else><i class="el-icon-circle-check" style="color: #00bb00"></i> {{ scope.row.taskName }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="returnSum" :label="$t('resource.risk_point')" min-width="25%">
                      <template slot-scope="scope">
                        <el-link v-if="!!scope.row.returnSum && scope.row.returnSum>0" style="color: red;" @click="innerDrawerOpen(scope.row)"> {{ scope.row.returnSum?scope.row.returnSum:0 }} / {{ scope.row.resourcesSum?scope.row.resourcesSum:0 }}</el-link>
                        <span v-else> {{ scope.row.returnSum?scope.row.returnSum:0 }} / {{ scope.row.resourcesSum?scope.row.resourcesSum:0 }}</span>
                      </template>
                    </el-table-column>
                </el-table>
              </span>
            </el-col>
          </el-row>
          <el-row class="el-row-c">
            <el-col :span="8"><span style="color: #909090;">{{ $t('resource.suggestions_for_improvement') }} <i class="el-icon-question"></i></span></el-col>
            <el-col :span="16"><span>{{ detailForm.improvement }}</span></el-col>
          </el-row>
          <template v-slot:footer>
            <dialog-footer
              @cancel="visible = false"
              @confirm="visible = false"/>
          </template>
          <el-drawer
            size="50%"
            :title="$t('rule.rule_detail')"
            :append-to-body="true"
            :before-close="innerDrawerClose"
            :visible.sync="innerDrawer">
            <el-form :model="ruleForm" label-position="right" label-width="120px" size="small" ref="ruleForm">
              <el-form-item :label="$t('rule.rule_name')">
                <el-input v-model="ruleForm.name" autocomplete="off" :placeholder="$t('rule.rule_name')"/>
              </el-form-item>
              <el-form-item :label="$t('rule.rule_description')">
                <el-input v-model="ruleForm.description" autocomplete="off" :placeholder="$t('rule.rule_description')"/>
              </el-form-item>
              <el-form-item :label="$t('account.cloud_platform')">
                <el-select style="width: 100%;" v-model="ruleForm.pluginId" :placeholder="$t('account.please_choose_plugin')">
                  <el-option
                    v-for="item in plugins"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                    <img :src="require(`@/assets/img/platform/${item.icon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                    &nbsp;&nbsp; {{ $t(item.name) }}
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('rule.rule_tag')">
                <el-select style="width: 100%;" multiple v-model="ruleForm.tags" :placeholder="$t('rule.please_choose_tag')">
                  <el-option
                    v-for="item in tags"
                    :key="item.tagKey"
                    :label="item.tagName"
                    :value="item.tagKey">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('rule.severity')">
                <el-select style="width: 100%;" v-model="ruleForm.severity" :placeholder="$t('rule.please_choose_severity')">
                  <el-option
                    v-for="item in severityOptions"
                    :key="item.value"
                    :label="item.key"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('rule.rule_set')">
                <el-select style="width: 100%;" multiple filterable v-model="ruleForm.ruleSets">
                  <el-option
                    v-for="item in ruleSetOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('rule.inspection_report')">
                <el-select style="width: 100%;" multiple filterable collapse-tags v-model="ruleForm.inspectionSeports">
                  <el-option
                    v-for="item in inspectionSeportOptions"
                    :key="item.id"
                    :label="item.id + '. ' + item.project.substring(0, 50) + '...'"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('rule.rule_yml')">
                <codemirror ref="cmEditor" v-model="ruleForm.script" class="code-mirror" :options="cmOptions" />
              </el-form-item>
              <el-form-item>
                <el-table
                  :data="ruleForm.parameter"
                  style="width: 100%">
                  <el-table-column v-slot:default="scope" label="Key" min-width="20%">
                    <el-input v-model="scope.row.key" :placeholder="$t('commons.input_content')"></el-input>
                  </el-table-column>
                  <el-table-column v-slot:default="scope" :label="$t('rule.middleware_name')" min-width="30%">
                    <el-input v-model="scope.row.name" :placeholder="$t('commons.input_content')"></el-input>
                  </el-table-column>
                  <el-table-column v-slot:default="scope" :label="$t('rule.middleware_parameter_default')" min-width="30%">
                    <el-input v-model="scope.row.defaultValue" :placeholder="$t('commons.input_content')"></el-input>
                  </el-table-column>
                  <el-table-column v-slot:default="scope" :label="$t('rule.required')" min-width="20%">
                    <el-switch v-model="scope.row.required" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
                  </el-table-column>
                </el-table>
              </el-form-item>
            </el-form>
          </el-drawer>
      </el-drawer>
      <!--Rule detail-->

    </main-container>
</template>

<script>
import TableOperators from "../../common/components/TableOperators";
import MainContainer from "../../common/components/MainContainer";
import Container from "../../common/components/Container";
import TableHeader from "../head/ResourceTableHeader";
import TablePagination from "../../common/pagination/TablePagination";
import TableOperator from "../../common/components/TableOperator";
import DialogFooter from "../../common/components/DialogFooter";
import CenterChart from "../../common/components/CenterChart";
import MetricChart from "./MetricChart";
import {_filter, _sort, getCurrentAccountID} from "@/common/js/utils";
import {ACCOUNT_ID} from "@/common/js/constants";
import {saveAs} from "@/common/js/FileSaver.js";

/* eslint-disable */
  export default {
    components: {
      TableOperators,
      MainContainer,
      Container,
      TableHeader,
      TablePagination,
      TableOperator,
      DialogFooter,
      CenterChart,
      MetricChart,
    },
    data() {
      return {
        result: {},
        content: {},
        tableData: [],
        groups: [],
        currentPage: 1,
        pageSize: 10,
        total: 0,
        loading: false,
        ruleForm: {parameter:[]},
        tags: [],
        plugins: [],
        severityOptions: [],
        ruleSetOptions: [],
        inspectionSeportOptions: [],
        condition: {
        },
        buttons: [
          {
            tip: this.$t('resource.i18n_detail'), icon: "el-icon-edit-outline", type: "primary",
            exec: this.handleDetails
          }
        ],
        platforms: [
          {text: this.$t('account.aliyun'), value: 'fit2cloud-aliyun-plugin'},
          {text: this.$t('account.tencent'), value: 'fit2cloud-qcloud-plugin'},
          {text: this.$t('account.huawei'), value: 'fit2cloud-huawei-plugin'},
          {text: this.$t('account.aws'), value: 'fit2cloud-aws-plugin'},
          {text: this.$t('account.azure'), value: 'fit2cloud-azure-plugin'},
          {text: this.$t('account.vsphere'), value: 'fit2cloud-vsphere-plugin'},
          {text: this.$t('account.openstack'), value: 'fit2cloud-openstack-plugin'}
        ],
        visible: false,
        accountId: localStorage.getItem(ACCOUNT_ID),
        direction: 'rtl',
        detailForm: {},
        innerDrawer: false,
        cmOptions: {
          tabSize: 4,
          mode: {
            name: 'shell',
            json: true
          },
          theme: 'bespin',
          lineNumbers: true,
          line: true,
          indentWithTabs: true,
        },
        groupId: "",
        groupName: "",
      }
    },
    methods: {
      sort(column) {
        _sort(column, this.condition);
        this.init();
      },
      filter(filters) {
        _filter(filters, this.condition);
        this.init();
      },
      async search () {
        if (!!getCurrentAccountID()) {
          this.accountId = getCurrentAccountID();
        }
        await this.groupSearch();
      },
      async groupSearch () {
        this.result = await this.$post("/resource/rule/groups", {accountId: this.accountId}, response => {
          this.groups = response.data;
          if(this.groups.length > 0) {
            this.groupId = this.groups[0].id;
            this.groupName = this.groups[0].name;
            this.reportIsoSearch();
          }
        });
      },
      async reportIsoSearch() {
        await this.$get("/resource/report/iso/" + this.accountId + '/' + this.groupId, response => {
          this.content = response.data;
          this.content.groupName = this.groupName;
          this.reportListSearch();
        });
      },
      async reportListSearch() {
        let url = "/resource/reportList/" + this.currentPage + "/" + this.pageSize;
        //在这里实现事件
        this.condition.accountId = this.accountId;
        this.condition.groupId = this.groupId;
        await this.$post(url, this.condition, response => {
          let data = response.data;
          this.total = data.itemCount;
          this.tableData = data.listObject;
        });
      },
      goResource (params) {
        this.$router.push({
          path: '/resource/resultdetails/' + params.id
        }).catch(error => error);
      },
      tagLists() {
        let url = "/rule/ruleTags";
        this.result = this.$get(url, response => {
          this.tags = response.data;
        });
      },
      //查询插件
      activePlugin() {
        let url = "/plugin/all";
        this.result = this.$get(url, response => {
          let data = response.data;
          this.plugins =  data;
        });
      },
      innerDrawerOpen (item) {
        this.innerDrawer = true;
        this.viewRule(item);
      },
      async viewRule (item) {
        await this.$get("/rule/getRuleByTaskId/" + item.id, response => {
          this.ruleForm = response.data;
          if (typeof(this.ruleForm.parameter) == 'string') this.ruleForm.parameter = JSON.parse(this.ruleForm.parameter);
          this.ruleForm.tagKey = this.ruleForm.tags[0];
        });
      },
      severityOptionsFnc () {
        this.severityOptions = [
          {key: '低风险', value: "LowRisk"},
          {key: '中风险', value: "MediumRisk"},
          {key: '高风险', value: "HighRisk"}
        ];
      },
      ruleSetOptionsFnc () {
        this.$get("/rule/ruleGroups/" + null, res => {
          this.ruleSetOptions = res.data;
        });
      },
      inspectionSeportOptionsFnc () {
        this.$get("/rule/all/ruleInspectionReport", res => {
          this.inspectionSeportOptions = res.data;
        });
      },
      init() {
        this.tagLists();
        this.activePlugin();
        this.severityOptionsFnc();
        this.ruleSetOptionsFnc();
        this.inspectionSeportOptionsFnc();
        this.search();
      },
      filterAccount (tag) {
        if (!!tag.name) {
          this.condition.pluginId = tag.name;
          this.search();
        }
      },
      tableRowClassName({row, rowIndex}) {
        if (rowIndex % 4 === 0) {
          return 'success-row';
        } else if (rowIndex % 2 === 0) {
          return 'warning-row';
        } else {
          return '';
        }
      },
      handleClick(tab) {
        for (let obj of this.groups) {
          if (tab.label == obj.name) {
            this.groupId = obj.id;
            this.groupName = obj.name;
            break;
          }
        }
        this.reportIsoSearch();
      },
      handleClose() {
        this.visible = false;
      },
      innerDrawerClose() {
        this.innerDrawer = false;
      },
      handleDetails (item) {
        this.detailForm = item;
        this.visible=true;
      },
      handleScan () {
        this.$get("/rule/reScan/" + item.id + "/" + item.accountId, response => {
          if (response.success) {
            this.search();
          }
        });
      },
      download() {
        let myDate = new Date();
        this.$alert(this.$t('resource.download_report_description_start') + myDate.toLocaleString() + this.$t('resource.download_report_description_end'), this.$t('resource.download_report'), {
          confirmButtonText: this.$t('commons.confirm'),
          callback: (action) => {
            if (action === 'confirm') {
              let columns = [
                {value: this.$t('resource.F2C_ID'), key: "f2cId"},
                {value: this.$t('dashboard.resource_name'), key: "resourceName"},
                {value: this.$t('rule.resource_type'), key: "resourceType"},
                {value: this.$t('account.region_id'), key: "regionId"},
                {value: this.$t('account.region_name'), key: "regionName"},
                {value: this.$t('rule.rule_name'), key: "ruleName"},
                {value: this.$t('rule.rule_description'), key: "ruleDescription"},
                {value: this.$t('rule.severity'), key: "severity"},
                {value: this.$t('resource.audit_name'), key: "auditName"},
                {value: this.$t('resource.basic_requirements_for_grade_protection'), key: "basicRequirements"},
                {value: this.$t('resource.suggestions_for_improvement'), key: "improvement"},
              ];
              this.result = this.$download("/resource/export/" + this.accountId, {
                columns: columns
              }, response => {
                if (response.status === 201) {
                  let blob = new Blob([response.data], {type: "'application/octet-stream'"});
                  saveAs(blob, this.$t("resource.resource_report_xlsx"));
                }
              }, error => {
                console.log("导出报错", error);
              });
            }
          }
        });
      },
    },
    created() {
      this.init();
    }
  }
</script>

<style scoped>
  .el-row {
    margin-bottom: 20px;
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #f2f2f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }

  .el-form-item-dev  >>> .el-form-item__content {
    margin-left: 0 !important;
  }

  .grid-content-log-span {
    width: 40%;float: left;
    vertical-align: middle;
    display:table-cell;
    margin: 6px 0;
  }

  .grid-content-status-span {
    width: 20%;float: left;
    vertical-align: middle;
    display:table-cell;
    margin: 6px 0;
  }

  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    padding: 10px 2%;
    width: 46%;
  }

  .el-icon-close-detail {
    float: right;
    cursor:pointer;
  }

  .view-text{
    display: inline-block;
    white-space: nowrap;
    width: 100%;
    overflow: hidden;
    text-overflow:ellipsis;
  }
  .text-click {
    color: #0066ac;
  }
  .table-card {
    margin-bottom: 15px;
  }
  .table-card >>> .el-card__body {
    padding: 0 !important;
  }

  .table-card >>> span {
    margin: 50px !important;
  }

  .report-container .el-tabs__header {
    margin-bottom: 1px;
  }

  .report-container {
    height: calc(100vh - 550px);
    min-height: 600px;
    overflow-y: auto;
  }
  .table-report-card {
    margin-bottom: 15px;
  }

  .table-report-card >>> .el-card__body {
    height: 170px;
  }

  .el-row-c {
    margin: 50px;
  }

  .border-card {
  }

  .rtl >>> .el-drawer{
    overflow: scroll;
  }
  /deep/ :focus{outline:0;}
</style>
