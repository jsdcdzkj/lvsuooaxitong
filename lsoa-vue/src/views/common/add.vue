<template>
    <div class="app-container">
        <div class="header-title">
            <div>新增案件</div>
            <el-button @click="handleBack" size="small">返回</el-button>
        </div>

        <div class="detail-content">
            <!-- 当事人信息 -->
            <div class="common-content">
                <div class="common-title">当事人</div>
                <el-form
                    :inline="true"
                    label-position="right"
                    class="baseInfo litigantInfo"
                    label-width="110px"
                >
                    <el-row :gutter="24">
                        <el-col :span="8" class="client-man">
                            <el-form-item label="委托当事人" prop="pod">
                                <el-select
                                    v-model="listQuery.pod"
                                    style="width: 100%"
                                    placeholder="请选择"
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <!-- 1.原告2.被告3.犯罪嫌疑人 4.第三人(非原被告)5.受害人" -->
                                    <el-option
                                        :disabled="listQuery.podOther == 1"
                                        label="原告"
                                        value="1"
                                    ></el-option>
                                    <el-option
                                        :disabled="listQuery.podOther == 2"
                                        label="被告"
                                        value="2"
                                    ></el-option>
                                    <el-option
                                        :disabled="listQuery.podOther == 3"
                                        label="犯罪嫌疑人"
                                        value="3"
                                    ></el-option>
                                    <el-option
                                        :disabled="listQuery.podOther == 4"
                                        label="第三人(非原被告)"
                                        value="4"
                                    ></el-option>
                                    <el-option
                                        :disabled="listQuery.podOther == 5"
                                        label="受害人"
                                        value="5"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="24">
                        <el-col :span="8" v-for="(item, index) in listQuery.plainList" :key="index">
                            <div class="card">
                                <div class="delete" @click="handleDelete(index)">
                                    <i
                                        class="el-icon-delete"
                                        style="color: #f56c6c; font-size: 20px"
                                    ></i>
                                </div>
                                <div class="avatar">
                                    <img src="@/assets/img/avatar.png" alt="" />
                                    <div class="tip">当事人{{ index + 1 }}</div>
                                </div>

                                <div class="client-man">
                                    <el-form-item
                                        label="姓名"
                                        v-if="item.isPerson == 1 || item.isPerson == 4"
                                    >
                                        <el-input
                                            v-model="listQuery.plainList[index].name"
                                            size="small"
                                            placeholder="请输入姓名"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item
                                        label="性别"
                                        v-if="item.isPerson == 1 || item.isPerson == 4"
                                    >
                                        <el-radio-group v-model="listQuery.plainList[index].sex">
                                            <el-radio label="1">男</el-radio>
                                            <el-radio label="2">女</el-radio>
                                        </el-radio-group>
                                    </el-form-item>
                                    <el-form-item
                                        class="code"
                                        label="身份证号"
                                        v-if="item.isPerson == 1 || item.isPerson == 4"
                                    >
                                        <el-input
                                            v-model="listQuery.plainList[index].code"
                                            size="small"
                                            placeholder="请输入身份证号"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item
                                        label="单位名称"
                                        v-if="item.isPerson == 2 || item.isPerson == 3"
                                    >
                                        <el-input
                                            v-model="listQuery.plainList[index].name"
                                            size="small"
                                            placeholder="请输入单位名称"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="单位名称" v-if="item.isPerson == 5">
                                        <!-- <el-input
                                            v-model="listQuery.plainList[index].name"
                                            size="small"
                                            placeholder="请输入单位名称"
                                            :maxlength="50"
                                            clearable
                                        /> -->
                                        <el-select
                                            v-model="listQuery.plainList[index].name"
                                            placeholder="请输入单位名称"
                                            clearable
                                            @change="(e) => handleClient(e, index)"
                                            filterable
                                            size="small"
                                        >
                                            <el-option
                                                v-for="(item, index) in customList"
                                                :key="index"
                                                :label="item.customerName"
                                                :value="item.customerName"
                                            ></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item
                                        label="统一代码"
                                        v-if="!(item.isPerson == 1 || item.isPerson == 4)"
                                    >
                                        <el-input
                                            v-model="listQuery.plainList[index].code"
                                            size="small"
                                            placeholder="请输入统一代码"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="联系方式">
                                        <el-input
                                            v-model="listQuery.plainList[index].phone"
                                            size="small"
                                            placeholder="请输入联系方式"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                </div>
                            </div>
                        </el-col>
                        <el-col :span="8">
                            <div class="card" style="justify-content: center; align-items: center">
                                <el-dropdown @command="handleAddman" style="height: 60px">
                                    <div class="add-man">
                                        <i
                                            class="el-icon-plus"
                                            style="
                                                font-size: 28px;
                                                font-weight: bold;
                                                color: rgba(0, 0, 0, 0.25);
                                                margin-bottom: 12px;
                                            "
                                        ></i>
                                        <div style="color: rgba(0, 0, 0, 0.45)">新增当事人</div>
                                    </div>
                                    <el-dropdown-menu slot="dropdown">
                                        <!-- 1自然人 2法人 3个体工商户 4受害人 5协议客户 -->
                                        <el-dropdown-item command="1">自然人</el-dropdown-item>
                                        <el-dropdown-item command="2">法人</el-dropdown-item>
                                        <el-dropdown-item command="3">个体工商户</el-dropdown-item>
                                        <el-dropdown-item command="4">受害人</el-dropdown-item>
                                        <el-dropdown-item command="5">顾问单位</el-dropdown-item>
                                    </el-dropdown-menu>
                                </el-dropdown>
                            </div>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="common-content">
                <div class="common-title">基本信息</div>
                <el-form
                    ref="formInline"
                    :inline="true"
                    :model="listQuery"
                    label-position="top"
                    :rules="rules"
                    class="baseInfo"
                    label-width="110px"
                >
                    <el-row :gutter="24">
                        <!-- <el-col :span="8">
                            <el-form-item label="案件名称" prop="caseName">
                                <el-input
                                    v-model="listQuery.caseName"
                                    size="small"
                                    placeholder="请输入案件名称"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col> -->
                        <el-col :span="8">
                            <el-form-item label="案件类型" prop="caseType">
                                <el-select
                                    v-model="listQuery.caseType"
                                    placeholder="请选择案件类型"
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <el-option
                                        v-for="(item, index) in caseList"
                                        :key="index"
                                        :label="item.dictLabel"
                                        :value="item.dictValue"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="业务类型" prop="businessType">
                                <el-select
                                    v-model="listQuery.businessType"
                                    placeholder="请选择业务类型"
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <el-option
                                        v-for="(item, index) in businessList"
                                        :key="index"
                                        :label="item.dictLabel"
                                        :value="item.dictValue"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8" v-if="selectCustom.length == 0">
                            <el-form-item label="费用（元）" prop="money">
                                <el-input-number
                                    v-model="listQuery.money"
                                    :controls="false"
                                    :precision="2"
                                    size="small"
                                    placeholder="请输入费用"
                                    :min="1"
                                    clearable
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="标的额" prop="subjectAmount">
                                <el-input-number
                                    v-model="listQuery.subjectAmount"
                                    size="small"
                                    :controls="false"
                                    :precision="2"
                                    placeholder="请输入标的额"
                                    :min="1"
                                    clearable
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="诉讼阶段" prop="stage">
                                <el-select
                                    v-model="listQuery.stage"
                                    placeholder="请选择诉讼阶段"
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <el-option
                                        v-for="(item, index) in litigationList"
                                        :key="index"
                                        :label="item.dictLabel"
                                        :value="item.dictValue"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="案件状态" prop="status">
                                <el-select
                                    v-model="listQuery.status"
                                    style="width: 100%"
                                    placeholder="请选择案件状态"
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <el-option
                                        v-for="(item, index) in caseStatusList"
                                        :key="index"
                                        :label="item"
                                        :value="`${index + 1}`"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="办案机构" prop="court">
                                <el-input
                                    v-model="listQuery.court"
                                    size="small"
                                    placeholder="请输入办案机构"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="特殊审批" prop="riskOrFree">
                                <el-radio-group v-model="listQuery.riskOrFree">
                                    <el-radio label="1">风险代理</el-radio>
                                    <el-radio label="2">费用减免</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="案由" prop="subjectOfAction">
                                <el-input
                                    v-model="listQuery.subjectOfAction"
                                    size="small"
                                    type="textarea"
                                    placeholder="请输入案由"
                                    clearable
                                    :maxlength="150"
                                />
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="common-content">
                <div class="common-title">对方当事人</div>
                <el-form
                    :inline="true"
                    label-position="right"
                    class="baseInfo litigantInfo"
                    label-width="110px"
                    style="border-top: 1px dashed rgba(0, 0, 0, 0.06)"
                >
                    <el-row :gutter="24">
                        <el-col :span="8" class="client-man">
                            <el-form-item label="对方当事人" prop="podOther">
                                <el-select
                                    v-model="listQuery.podOther"
                                    style="width: 100%"
                                    placeholder="请选择"
                                    clearable
                                    filterable
                                    size="small"
                                >
                                    <el-option
                                        :disabled="listQuery.pod == 1"
                                        label="原告"
                                        value="1"
                                    ></el-option>
                                    <el-option
                                        :disabled="listQuery.pod == 2"
                                        label="被告"
                                        value="2"
                                    ></el-option>
                                    <el-option
                                        :disabled="listQuery.pod == 3"
                                        label="犯罪嫌疑人"
                                        value="3"
                                    ></el-option>
                                    <el-option
                                        :disabled="listQuery.pod == 4"
                                        label="第三人(非原被告)"
                                        value="4"
                                    ></el-option>
                                    <el-option
                                        :disabled="listQuery.pod == 5"
                                        label="受害人"
                                        value="5"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="24">
                        <el-col
                            :span="8"
                            v-for="(item, index) in listQuery.defendList"
                            :key="index"
                        >
                            <div class="card">
                                <div class="delete" @click="handleOtherDelete(index)">
                                    <i
                                        class="el-icon-delete"
                                        style="color: #f56c6c; font-size: 20px"
                                    ></i>
                                </div>
                                <div class="avatar">
                                    <img src="@/assets/img/avatar.png" alt="" />
                                    <div class="tip">当事人{{ index + 1 }}</div>
                                </div>

                                <div class="client-man">
                                    <el-form-item
                                        label="姓名"
                                        v-if="item.isPerson == 1 || item.isPerson == 4"
                                    >
                                        <el-input
                                            v-model="listQuery.defendList[index].name"
                                            size="small"
                                            placeholder="请输入姓名"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item
                                        label="性别"
                                        v-if="item.isPerson == 1 || item.isPerson == 4"
                                    >
                                        <el-radio-group v-model="listQuery.defendList[index].sex">
                                            <el-radio label="1">男</el-radio>
                                            <el-radio label="2">女</el-radio>
                                        </el-radio-group>
                                    </el-form-item>
                                    <el-form-item
                                        class="code"
                                        label="身份证号"
                                        v-if="item.isPerson == 1 || item.isPerson == 4"
                                    >
                                        <el-input
                                            v-model="listQuery.defendList[index].code"
                                            size="small"
                                            placeholder="请输入身份证号"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item
                                        label="单位名称"
                                        v-if="!(item.isPerson == 1 || item.isPerson == 4)"
                                    >
                                        <el-input
                                            v-model="listQuery.defendList[index].name"
                                            size="small"
                                            placeholder="请输入单位名称"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item
                                        label="统一代码"
                                        v-if="!(item.isPerson == 1 || item.isPerson == 4)"
                                    >
                                        <el-input
                                            v-model="listQuery.defendList[index].code"
                                            size="small"
                                            placeholder="请输入统一代码"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                    <el-form-item label="联系方式">
                                        <el-input
                                            v-model="listQuery.defendList[index].phone"
                                            size="small"
                                            placeholder="请输入联系方式"
                                            :maxlength="50"
                                            clearable
                                        />
                                    </el-form-item>
                                </div>
                            </div>
                        </el-col>
                        <el-col :span="8">
                            <div class="card" style="justify-content: center; align-items: center">
                                <el-dropdown @command="handleAddDefend" style="height: 60px">
                                    <div class="add-man">
                                        <i
                                            class="el-icon-plus"
                                            style="
                                                font-size: 28px;
                                                font-weight: bold;
                                                color: rgba(0, 0, 0, 0.25);
                                                margin-bottom: 12px;
                                            "
                                        ></i>
                                        <div style="color: rgba(0, 0, 0, 0.45)">新增当事人</div>
                                    </div>
                                    <el-dropdown-menu slot="dropdown">
                                        <el-dropdown-item command="1">自然人</el-dropdown-item>
                                        <el-dropdown-item command="2">法人</el-dropdown-item>
                                        <el-dropdown-item command="3">个体工商户</el-dropdown-item>
                                        <el-dropdown-item command="4">受害人</el-dropdown-item>
                                    </el-dropdown-menu>
                                </el-dropdown>
                            </div>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <div class="common-content" v-if="selectCustom.length == 0">
                <div class="common-title">附件管理（必须上传合同文件）</div>
                <div class="annex-wrap">
                    <el-radio-group v-model="annexType" size="mini">
                        <el-radio-button v-for="(item,index) in fileTypeList" :key="index" @change="handleChange(item.dictValue)" :label="item.dictValue">{{ item.dictLabel }}</el-radio-button>
                        <el-radio-button v-if="otherFile && otherFile.length > 0"  label="-1" @change="handleChange('-1')">其他</el-radio-button>
                    </el-radio-group>
                    <div style="width: 100%" v-for="(item,index) in fileTypeList" :key="index">
                        <handleRequired v-show="annexType == item.dictValue" :id="item.dictValue" edit
                        :fileList="(listQuery.fileList && listQuery.fileList[item.dictValue]) || []"
                        @add="handleAddIds" @del="handleDelIds" @changeFile="changeFile" />
                    </div>
                    <div style="width: 100%" v-show="annexType == '-1'">
                        <handleRequired id="-1"
                        :fileList="otherFile || []"
                        edit
                        isOther
                        @add="handleAddIds" @del="handleDelIds" />
                    </div>
                </div>
            </div>
            <div class="common-content" v-if="selectCustom.length == 0">
                <div class="common-title">开票信息（选择顾问客户不显示开票信息）</div>
                <el-form
                    ref="formInvoice"
                    :inline="true"
                    :model="invoiceInfoVo"
                    label-position="top"
                    :rules="rules"
                    class="baseInfo"
                    label-width="110px"
                >
                    <el-row :gutter="24">
                        <el-col :span="8">
                            <el-form-item label="抬头类型" prop="titleType">
                                <el-radio-group v-model="invoiceInfoVo.titleType">
                                    <el-radio label="1">个人</el-radio>
                                    <el-radio label="2">单位</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="抬头名称" prop="title">
                                <el-input
                                    v-model="invoiceInfoVo.title"
                                    size="small"
                                    @focus="handleTitleFocus"
                                    placeholder="请输入抬头名称"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="税号" prop="number">
                                <el-input
                                    v-model="invoiceInfoVo.number"
                                    size="small"
                                    @focus="handleNumberFocus"
                                    placeholder="请输入税号"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="联系方式" prop="contactPhone">
                                <el-input
                                    v-model="invoiceInfoVo.contactPhone"
                                    size="small"
                                    @focus="handleContactPhoneFocus"
                                    placeholder="请输入联系方式"
                                    :maxlength="50"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="开票金额" prop="invoiceMoney">
                                <el-input-number
                                    v-model="invoiceInfoVo.invoiceMoney"
                                    :controls="false"
                                    :precision="2"
                                    size="small"
                                    @focus="handleInvoiceMoneyFocus"
                                    placeholder="请输入开票金额"
                                    :min="1"
                                    clearable
                                ></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="24">
                            <el-form-item label="发票内容" prop="content">
                                <el-input
                                    v-model="invoiceInfoVo.content"
                                    size="small"
                                    type="textarea"
                                    placeholder="请输入发票内容"
                                    clearable
                                />
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
        </div>

        <div class="foot-wrap">
            <el-button @click="handleBack" size="small">取消</el-button>
            <el-button
                type="success"
                size="small"
                :disabled="disabled"
                :loading="disabled"
                @click="save('formInline', 1)"
                >保存</el-button
            >
            <el-button
                type="primary"
                size="small"
                :disabled="disabled"
                :loading="disabled"
                @click="save('formInline', 2)"
                >提交</el-button
            >
        </div>
    </div>
</template>

<script>
import handleRequired from './handleRequired.vue';
import { getRedisDictList, getCaseFileType } from '@/api/common';
import { saveCaseInfo, getCustomList, getEntity } from '@/api/case';
import { invoiceTypeList, letterTypeList, caseStatusList } from '@/utils/comvariable';

export default {
    components: { handleRequired },
    data() {
        return {
            query: {},
            listQuery: {
                plainList: [],
                defendList: [],
                fileIdList: [],
                pod: '',
                podOther: '',
            },
            litigantList: [{}, {}],
            disabled: false,
            annexType: 0,
            businessList: [],
            caseList: [],
            litigationListL: [],
            invoiceTypeList,
            letterTypeList,
            caseStatusList,
            litigationList: [],
            fileTypeList: [],
            otherFile: [],
            rules: {
                subjectAmount: [{ required: true, message: '请输入', trigger: 'blur' }],
                caseName: [{ required: true, message: '请输入', trigger: 'blur' }],
                caseType: [{ required: true, message: '请选择', trigger: 'blur' }],
                businessType: [{ required: true, message: '请输入', trigger: 'blur' }],
                money: [{ required: true, trigger: 'blur', message: '请输入' }],
                court: [{ required: true, trigger: 'blur', message: '请输入' }],
                subjectOfAction: [{ required: true, trigger: 'blur', message: '请输入' }],
                stage: [{ required: true, trigger: 'blur', message: '请选择' }],
                status: [{ required: true, message: '请选择', trigger: 'blur' }],
                caseDesc: [{ required: true, message: '请输入', trigger: 'blur' }],
                // number: [{ required: true, message: '请输入', trigger: 'blur' }],
                money: [{ required: true, message: '请输入', trigger: 'blur' }],
                subjectAmount: [{ required: true, message: '请输入', trigger: 'blur' }],
                personType: [{ required: true, message: '请选择', trigger: 'blur' }],
                titleType: [{ required: true, message: '请选择', trigger: 'blur' }],
                title: [{ required: true, message: '请输入', trigger: 'blur' }],
                invoiceMoney: [{ required: true, message: '请输入', trigger: 'blur' }],
                contactPhone: [
                    { required: true, message: '请输入', trigger: 'blur' },
                    {
                        validator: function (rule, value, callback) {
                            if (
                                /^1[3456789]\d{9}$/.test(value) == false &&
                                /^((0\d{2,3})-)?(\d{7,8})$/.test(value) == false
                            ) {
                                callback(new Error('请输入正确的联系方式'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
                name: [{ required: true, message: '请输入', trigger: 'blur' }],
                // code: [{ required: true, message: '请输入', trigger: 'blur' }],
                phone: [
                    { required: true, message: '请输入', trigger: 'blur' },
                    {
                        validator: function (rule, value, callback) {
                            if (
                                /^1[3456789]\d{9}$/.test(value) == false &&
                                /^((0\d{2,3})-)?(\d{7,8})$/.test(value) == false
                            ) {
                                callback(new Error('请输入正确的联系方式'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'blur',
                    },
                ],
                printType: [{ required: true, message: '请选择', trigger: 'blur' }],
                type: [{ required: true, message: '请选择', trigger: 'blur' }],
                level: [{ required: true, message: '请选择', trigger: 'blur' }],
            },
            customList: [],
            selectCustom: [],
            printTypeList: [],
            summonsInfoVo: {
                personType:'',
                name: '',
                code: '',
                phone:'',
            },
            invoiceInfoVo: {
                titleType: '',
                title: '',
                number: '',
                contactPhone:'',
                invoiceMoney: undefined
            },
            clientDelIds: [],
            contractList: [], // 判断是否上传了合同发票
        };
    },
    created() {
    },
    mounted() {
        getCustomList({}).then((res) => {
            console.log('5555555555555555res', res);
            this.customList = res.data;
        });
    },
    methods: {
        showEdit(row) {
            this.getInfo()
            this.clientDelIds = []
            this.otherFile = []
            this.selectCustom = []
            this.summonsInfoVo = {
                personType:'',
                name: '',
                code: '',
                phone:'',
            }
            this.invoiceInfoVo = {
                titleType: '',
                title: '',
                number: '',
                contactPhone:'',
                invoiceMoney: undefined
            }
            this.listQuery = {
                plainList: [],
                defendList: [],
                fileIdList: [],
                fileList:{},
                pod: '',
                podOther: '',
            }
            if(row) {
                this.getDetail(row)
            }
            
        },
        getInfo() {
            // 案件类型
            getRedisDictList({ dictType: 'caseType' }).then((res) => {
                this.caseList = res.data;
            });
            // 业务类型
            getRedisDictList({ dictType: 'businessType' }).then((res) => {
                this.businessList = res.data;
            });
            // 诉讼阶段
            getRedisDictList({ dictType: 'litigationStage' }).then((res) => {
                this.litigationList = res.data;
            });
            // 文件类型
            getCaseFileType({}).then(res => {
                this.fileTypeList = res.data
                this.annexType = res.data[0].dictValue
            })
            // 用印类型
            getRedisDictList({ dictType: 'printType' }).then((res) => {
                this.printTypeList = res.data;
            });
        },
        getDetail(row) {
            getEntity({id: row.id}).then(res => {
                const that = this
                this.listQuery = {
                    ...res.data,
                    plainList: res.data.plainList ? res.data.plainList : [],
                    defendList: res.data.defendList ? res.data.defendList : [],
                    pod: '',
                    podOther: '',
                    subjectAmount: res.data.subjectAmount? res.data.subjectAmount : undefined,
                    money: res.data.money? res.data.money : undefined,
                }
                this.listQuery.pod = this.listQuery.plainList?.[0]?.pod
                this.listQuery.podOther = this.listQuery.defendList?.[0]?.pod              
                this.summonsInfoVo = this.listQuery.summonsJson ? {...this.summonsInfoVo, ...JSON.parse(this.listQuery.summonsJson)} : this.summonsInfoVo
                this.invoiceInfoVo = this.listQuery.invoiceJson ? {...this.invoiceInfoVo, ...JSON.parse(this.listQuery.invoiceJson)} : this.invoiceInfoVo
                this.selectCustom = []
                this.listQuery.plainList?.map((val, index) => {
                    if(val.customerId) {
                        this.selectCustom[index]= val.customerId
                    }
                    
                })
                that.listQuery.fileIdList =[]
                const keys = Object.keys(this.listQuery.fileList)
                keys.map(val => {
                    this.listQuery.fileList[val].map(item => {
                        that.listQuery.fileIdList.push(item.id)
                    })
                    if (!this.fileTypeList.some(item => item.dictValue == val)) {
                        this.otherFile.push(...this.listQuery.fileList[val])
                    }
                })
            })
        },
        handleAddman(value) {
            this.listQuery.plainList.push({
                name: '',
                code: '',
                phone: '',
                sex: '',
                isPerson: value,
                plainOrDefend: 1,
            });
        },
        handleDelete(index) {
            const arr = this.listQuery.plainList.splice(index, 1);
            this.selectCustom.splice(index, 1);
            const id = arr[0]?.id
            if(id) this.clientDelIds.push(id)
            console.log('33333333333this.clientDelIds', this.clientDelIds)
        },
        handleAddDefend(value) {
            this.listQuery.defendList.push({
                name: '',
                code: '',
                phone: '',
                sex: '',
                isPerson: value,
                plainOrDefend: 2,
            });
        },
        handleOtherDelete(index) {
            const arr = this.listQuery.defendList.splice(index, 1);
            const id = arr[0]?.id
            if(id) this.clientDelIds.push(id)
        },
        handleBack() {
            this.listQuery.archived = '';
            this.selectCustom = [];
            this.$emit('back');
            this.$refs.formInline.clearValidate()
            this.$refs.formInvoice.clearValidate()
            this.$refs.formLetter.clearValidate()
        },
        handleChange(val) {
            this.annexType = val
        },
        handleAddIds(arr) {
            console.log('4444444444444arr删除文件', arr)
            this.listQuery.fileIdList.push(arr)
        },
        handleDelIds(arr) {
            console.log('55555555555arr删除文件', arr)
            this.listQuery.fileIdList = this.listQuery.fileIdList.filter(val => val != arr)
        },
        changeFile(id, file) {
            this.listQuery.fileList[id] = file
        },
        // 协议客户改变当事人
        handleClient(e, index) {
            const obj = this.customList.find((o) => o.customerName == e);
            this.selectCustom[index] = obj.id;
            this.listQuery.plainList[index].customerId = obj.id;
            this.listQuery.plainList[index].code = obj.creditCode;
            this.listQuery.plainList[index].phone = obj.phone;
        },
        close() {
            this.listQuery.archived = '';
            this.selectCustom = [];
        },
        // 判断开票信息获取焦点时
        handleTitleFocus(e) {
            if(!e.target.value) {
                this.invoiceInfoVo.title = this.listQuery.plainList[0]?.name
            }
        },
        // 判断开票信息税号获取焦点时
        handleNumberFocus(e) {
            if(!e.target.value) {
                this.invoiceInfoVo.number = this.listQuery.plainList[0]?.code
            }
        },
        // 判断开票信息联系方式获取焦点时
        handleContactPhoneFocus(e) {
            if(!e.target.value) {
                this.invoiceInfoVo.contactPhone = this.listQuery.plainList[0]?.phone
            }
        },
        // 判断开票信息开票金额获取焦点时
        handleInvoiceMoneyFocus(e) {

            if(!e.target.value) {
                this.$set(this.invoiceInfoVo, 'invoiceMoney', this.listQuery?.money)
            }
        },
        save(formName, type) {
            const that = this;
            if (type == 1) {
                if(!this.listQuery.caseType || !this.listQuery.subjectOfAction) {
                    return this.$message({
                            type: 'error',
                            message: '保存必须填写案件类型、案由!',
                        })
                }
                this.disabled = true;
                this.listQuery.plainList = this.listQuery.plainList?.map((val) => {
                    val.pod = this.listQuery.pod;
                    return val;
                });
                this.listQuery.defendList = this.listQuery.defendList?.map((val) => {
                    val.pod = this.listQuery.podOther;
                    return val;
                });
                this.listQuery.clientInfoVoList = this.listQuery.plainList?.concat(
                    this.listQuery.defendList
                );
                this.listQuery.customerId =
                    this.selectCustom.filter((val) => val).join(',') || null;
                this.summonsInfoVo = {
                    personType:this.listQuery.plainList?.[0]?.isPerson,
                    name: this.listQuery.plainList?.[0]?.name,
                    code: this.listQuery.plainList?.[0]?.code,
                    phone: this.listQuery.plainList?.[0]?.phone,
                }
                saveCaseInfo({
                    ...this.listQuery,
                    delegate: this.listQuery.customerId ? 1 : 2,
                    temporarily: type,
                    summonsInfoVo: this.summonsInfoVo,
                    invoiceInfoVo: {...this.invoiceInfoVo, invoiceType: 1},
                    clientDelIds: this.clientDelIds.length > 0 ? this.clientDelIds : null
                })
                    .then((res) => {
                        this.$emit('back');
                        if (res.code == 0) {
                            setTimeout(() => {
                                this.disabled = false;
                            }, 1000);
                        }
                    })
                    .finally(() => {
                        this.disabled = false;
                    });
                return;
            }
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    console.log('9999999999999999this.selectCustom', this.selectCustom);
                    if (this.listQuery.plainList.length == 0)
                        return this.$message({
                            type: 'error',
                            message: '至少填写一位委托当事人!',
                        });
                    if (!this.listQuery.pod)
                        return this.$message({
                            type: 'error',
                            message: '请选择委托当事人类型!',
                        });
                    for (let i = 0; i < this.listQuery.plainList.length; i++) {
                        if (!this.listQuery.plainList[i].name) {
                            return this.$message({
                                type: 'error',
                                message: '请输入委托当事人名称!',
                            });
                        }
                        if (
                            ['1', '4'].includes(this.listQuery.plainList[i].isPerson) &&
                            !this.listQuery.plainList[i].sex
                        ) {
                            return this.$message({
                                type: 'error',
                                message: '请选择委托当事人性别!',
                            });
                        }
                        if (
                            ['2', '3', '5'].includes(this.listQuery.plainList[i].isPerson) &&
                            !this.listQuery.plainList[i].code
                        ) {
                            return this.$message({
                                type: 'error',
                                message: '请选择委托当事人公司统一代码!',
                            });
                        }
                        // if (['1', '4'].includes(this.listQuery.plainList[i].isPerson) && /(^\d{15}$)|(^\d{17}(\d|X|x)$)/.test(this.listQuery.plainList[i].code) == false) {
                        //     return this.$message({
                        //         type: 'error',
                        //         message: '请选择委托人当事人正确的身份证号码!',
                        //     })
                        // }
                        if (!this.listQuery.plainList[i].phone) {
                            return this.$message({
                                type: 'error',
                                message: '请输入委托当事人联系方式!',
                            });
                        }
                        var patrn = /^1[3456789]\d{9}$/; //手机电话
                        var zjdh = /^((0\d{2,3})-)?(\d{7,8})$/; //座机电话
                        if (
                            patrn.test(this.listQuery.plainList[i].phone) == false &&
                            zjdh.test(this.listQuery.plainList[i].phone) == false
                        ) {
                            return this.$message({
                                type: 'error',
                                message: '请输入正确的委托当事人联系方式!',
                            });
                        }
                    }
                    

                    if(this.listQuery.caseType != 1) {
                        if (!this.listQuery.podOther)
                        return this.$message({
                            type: 'error',
                            message: '请选择对方当事人类型!',
                        });
                        if (this.listQuery.defendList.length == 0)
                        return this.$message({
                            type: 'error',
                            message: '至少填写一位对方当事人!',
                        });
                        for (let i = 0; i < this.listQuery.defendList.length; i++) {
                        if (!this.listQuery.defendList[i].name) {
                            return this.$message({
                                type: 'error',
                                message: '请输入对方当事人名称!',
                            });
                        }
                        if (
                            ['1', '4'].includes(this.listQuery.defendList[i].isPerson) &&
                            !this.listQuery.defendList[i].sex
                        ) {
                            return this.$message({
                                type: 'error',
                                message: '请选择对方当事人性别!',
                            });
                        }
                        if (
                            ['2', '3', '5'].includes(this.listQuery.defendList[i].isPerson) &&
                            !this.listQuery.defendList[i].code
                        ) {
                            return this.$message({
                                type: 'error',
                                message: '请选择对方当事人公司统一代码!',
                            });
                        }
                        // if (['1', '4'].includes(this.listQuery.defendList[i].isPerson) && /(^\d{15}$)|(^\d{17}(\d|X|x)$)/.test(this.listQuery.defendList[i].code) == false) {
                        //     return this.$message({
                        //         type: 'error',
                        //         message: '请选择对方当事人正确的身份证号码!',
                        //     })
                        // }
                        if (!this.listQuery.defendList[i].phone) {
                            return this.$message({
                                type: 'error',
                                message: '请输入对方当事人联系方式!',
                            });
                        }
                        var patrn = /^1[3456789]\d{9}$/; //手机电话
                        var zjdh = /^((0\d{2,3})-)?(\d{7,8})$/; //座机电话
                        if (
                            patrn.test(this.listQuery.defendList[i].phone) == false &&
                            zjdh.test(this.listQuery.defendList[i].phone) == false
                        ) {
                            return this.$message({
                                type: 'error',
                                message: '请输入正确的对方当事人联系方式!',
                            });
                        }
                    }
                    }
                    
                    // 开函开票必传字段
                    await that.$refs.formInvoice?.validate();
                    this.disabled = true;
                    this.listQuery.plainList = this.listQuery.plainList.map((val) => {
                        val.pod = this.listQuery.pod;
                        return val;
                    });
                    this.listQuery.defendList = this.listQuery.defendList.map((val) => {
                        val.pod = this.listQuery.podOther;
                        return val;
                    });
                    this.listQuery.clientInfoVoList = this.listQuery.plainList.concat(
                        this.listQuery.defendList
                    );
                    this.listQuery.customerId =
                        this.selectCustom.filter((val) => val).join(',') || null;

                    this.summonsInfoVo = {
                        personType:this.listQuery.plainList?.[0]?.isPerson,
                        name: this.listQuery.plainList?.[0]?.name,
                        code: this.listQuery.plainList?.[0]?.code,
                        phone: this.listQuery.plainList?.[0]?.phone,
                    }

                    saveCaseInfo({
                        ...this.listQuery,
                        delegate: this.listQuery.customerId ? 1 : 2,
                        temporarily: type,
                        summonsInfoVo: this.summonsInfoVo,
                        invoiceInfoVo: {...this.invoiceInfoVo, invoiceType: 1},
                        clientDelIds: this.clientDelIds.length > 0 ? this.clientDelIds : null
                    })
                        .then((res) => {
                            this.$emit('back');
                            if (res.code == 0) {
                                setTimeout(() => {
                                    this.disabled = false;
                                }, 1000);
                            }
                        })
                        .finally(() => {
                            this.disabled = false;
                        });
                }
            });
        },
    },
};
</script>

<style lang="scss" scoped>
.header-title {
    width: 100%;
    height: 64px;
    background: #ffffff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 24px;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 10;
    box-shadow: inset 0px 0px 32px 0px rgba(198, 215, 225, 0.64);
}

.foot-wrap {
    width: 100%;
    height: 64px;
    background: #ffffff;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding: 24px;
    position: absolute;
    bottom: 0;
    left: 0;
    z-index: 10;
    box-shadow: inset 0px 0px 32px 0px rgba(198, 215, 225, 0.64);
}
.detail-content {
    margin: 60px 0;

    .el-form-item--small.el-form-item {
        width: 100%;
    }
}
::v-deep.litigantInfo {
    .el-form-item__content {
        width: calc(100% - 110px);
    }
}

::v-deep {
    .client-man {
        .el-form-item__label {
            &::before {
                content: '*';
                color: #f56c6c;
                margin-right: 4px;
            }
        }
    }

    .code {
        .el-form-item__label {
            &::before {
                content: '';
            }
        }
    }
}

.baseInfo {
    padding: 24px 24px 0;
}

.card {
    position: relative;
    display: flex;
    padding: 24px;
    height: 224px;
    background: #ffffff;
    box-shadow: 0px 0px 12px 0px rgba(230, 237, 241, 0.5);
    border-radius: 16px 16px 16px 16px;
    border: 1px solid rgba(0, 0, 0, 0.06);
    margin-bottom: 24px;

    .delete {
        width: 48px;
        height: 48px;
        background: #fef0f0;
        border-radius: 0px 16px 0px 16px;
        position: absolute;
        bottom: 0;
        left: 0;
        z-index: 1;
        line-height: 48px;
        text-align: center;
    }

    .avatar {
        position: relative;
        width: 72px;
        height: 72px;
        border-radius: 72px;
        overflow: hidden;

        img {
            width: 100%;
            height: 100%;
        }

        .tip {
            position: absolute;
            text-align: center;
            left: 0;
            bottom: 0;
            width: 72px;
            height: 20px;
            line-height: 20px;
            color: #ffffff;
            font-size: 10px;
            background: #409eff;
        }
    }

    .client-man {
        flex: 1;
        .el-form-item__content {
            width: calc(100% - 110px);
        }
    }

    .add-man {
        width: 70px;
        height: 60px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }
}

.annex-wrap {
    padding: 24px;
    display: flex;
    flex-direction: column;
    align-items: center;

    ::v-deep .el-radio-button--mini .el-radio-button__inner {
        padding: 10px 15px;
        margin-bottom: 24px;
    }
}

::v-deep {
    .el-dropdown-menu__item {
        padding: 0 36px;
    }
}
</style>
