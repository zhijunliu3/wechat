<template>
  <div class="app-container">
    <!-- 查询条件区域 -->
    <ssa-form
      ref="query"
      :form-props="conditions"
      :form="params"
      operation-type="query"
      @on-change="onChange"
    />
    <!-- 按钮组区域 -->
    <el-row class="button-bar">
      <el-button v-permission="[200]" size="mini" type="primary" @click="clickAdd">新增</el-button>
    </el-row>
    <!-- 表格区域 -->
    <ssa-table
      ref="table"
      data="/${obj.humpClassName}/query"
      :columns="columns"
      :request="{method: 'POST', body: params, allInBody: true}"
    >
      <template #opertions="{scope}">
        <el-tooltip v-permission="[201]" class="item" effect="dark" content="编辑" placement="top">
          <el-button size="mini" icon="el-icon-edit" circle @click="clickEdit(scope.row)" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="查看" placement="top">
          <el-button size="mini" icon="el-icon-view" circle @click="clickView(scope.row)" />
        </el-tooltip>
        <el-tooltip v-permission="[202]" class="item" effect="dark" content="删除" placement="top">
          <el-button size="mini" icon="el-icon-delete" circle @click="clickDelete(scope.row)" />
        </el-tooltip>
      </template>
    </ssa-table>
    <!-- 表单区域 -->
    <el-dialog :title="dialogTiele" :visible.sync="dialogVisible" width="800px">
      <ssa-form
        ref="form"
        :form-props="formProps"
        :form="form"
        :operation-type="operationType"
        @on-submit="onSubmit"
        @on-cancel="dialogVisible = false"
      />
    </el-dialog>
  </div>
</template>

<script>
import { add, edit, deleteRow } from '@/api/${obj.humpClassName}'
import { updateFormFieldStatus } from '@/utils/form'
export default {
  name: '${obj.className}',
  data() {
    return {
      conditions: {
        contents: [
          <#list obj.fields as field>
          {
            label: '${field.label}',
            name: '${field.name}',
            type: 'input'
          },
          </#list>
        ]
      },
      params: {},
      columns: [
        <#list obj.fields as field>
        { props: { prop: '${field.name}', label: '${field.label}' }},
        </#list>
        { render: 'opertions', props: { label: '操作', width: '220px' }}
      ],
      dialogVisible: false,
      dialogTiele: '${obj.tableInfo}--新增',
      formProps: {
        span: 12,
        contents: [
          <#list obj.fields as field>
          {
            label: '${field.label}',
            name: '${field.name}',
            type: 'input',
            required: true
          },
          </#list>
        ]
      },
      form: {},
      operationType: 'add'
    }
  },
  created() {
  },
  methods: {
    onChange() {
      this.$refs.table.reload()
    },
    clickAdd() {
      this.operationType = 'add'
      this.dialogTiele = '${obj.tableInfo}--新增'
      this.updateFormFieldStatus()
      this.form = {
        <#list obj.fields as field>
        ${field.name}: '',
        </#list>
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.invokeFormMethod('clearValidate')
      })
    },
    clickEdit(row) {
      this.operationType = 'edit'
      this.dialogTiele = '${obj.tableInfo}--编辑'
      this.updateFormFieldStatus()
      this.form = {
        <#list obj.fields as field>
        ${field.name}: row.${field.name},
        </#list>
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.invokeFormMethod('clearValidate')
      })
    },
    clickView(row) {
      this.operationType = 'view'
      this.dialogTiele = '${obj.tableInfo}--查看'
      this.updateFormFieldStatus()
      this.form = {
        <#list obj.fields as field>
        ${field.name}: row.${field.name},
        </#list>
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.invokeFormMethod('clearValidate')
      })
    },
    clickDelete(row) {
      this.$confirm(`此操作删除<#noparse>${</#noparse>row.${obj.primaryKeys[0].name}}?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        <#if obj.primaryKeys?size == 1>
        deleteRow(row.${obj.primaryKeys[0].name}).then(res => {
          this.$message.success('删除成功')
          this.$refs.table.reload()
        })
        </#if>
      })
    },
    onSubmit() {
      if (this.operationType === 'add') {
        this.add()
      } else if (this.operationType === 'edit') {
        this.edit()
      }
    },
    add() {
      add(this.form).then(res => {
        this.$message.success('新增成功')
        this.$refs.table.reload()
        this.dialogVisible = false
      })
    },
    edit() {
      edit(this.form).then(res => {
        this.$message.success('修改成功')
        this.$refs.table.reload()
        this.dialogVisible = false
      })
    },
    updateFormFieldStatus() {
      if (this.operationType === 'add') {
        updateFormFieldStatus(this.formProps, [])
      } else if (this.operationType === 'edit') {
        updateFormFieldStatus(this.formProps, [])
      } else if (this.operationType === 'view') {
        updateFormFieldStatus(this.formProps, [])
      }
    }
  }
}
</script>

<style lang="scss" scoped>

</style>

