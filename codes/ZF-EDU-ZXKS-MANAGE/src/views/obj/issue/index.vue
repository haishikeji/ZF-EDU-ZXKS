<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="题目" prop="issue">
                <a-input v-model="queryParam.issue" placeholder="请输入题目" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="类型" prop="type">
                <a-select placeholder="请选择类型" v-model="queryParam.type" style="width: 100%" allow-clear>
                  <a-select-option v-for="(d, index) in typeOptions" :key="index" :value="d.dictValue">{{
                    d.dictLabel
                  }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <!-- <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} "> -->
              <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
              <!-- <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'"/>
                </a>
              </span> -->
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 操作 -->
      <div class="table-operations">
        <a-button type="primary" @click="$refs.createForm.handleAdd(chapterId)" v-hasPermi="['obj:issue:add']">
          <a-icon type="plus" />新增
        </a-button>
        <a-button @click="openIssue" v-hasPermi="['obj:issue:add']"> <a-icon type="upload" />导入 </a-button>
        <a-button type="primary" @click="handleDownload" v-hasPermi="['obj:issue:add']">
          <a-icon type="download" />下载导入模板
        </a-button>
        <!-- <a-button type="primary" :disabled="single" @click="$refs.createForm.handleUpdate(undefined, ids,courseId)" v-hasPermi="['obj:issue:edit']">
          <a-icon type="edit" />修改
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['obj:issue:remove']">
          <a-icon type="delete" />删除
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['obj:issue:export']">
          <a-icon type="download" />导出
        </a-button> -->
        <a-button
          type="dashed"
          shape="circle"
          :loading="loading"
          :style="{ float: 'right' }"
          icon="reload"
          @click="getList"
        />
      </div>
      <a-modal title="上传题库" :visible="dialogVisibleIssue" :width="600" :footer="null" @cancel="colseDialogIssue">
        <a-upload-dragger
          class="fileupload"
          name="file"
          :multiple="false"
          accept=".xlsx, .xls"
          :before-upload="handleChange"
          :action="getUploadUrl"
        >
          <p class="ant-upload-drag-icon">
            <a-icon type="inbox" />
          </p>
          <p class="ant-upload-text">点击或拖拽文件</p>
          <p class="ant-upload-hint">提示：仅允许导入"xls"或"xlsx"格式文件</p>
        </a-upload-dragger>
      </a-modal>

      <a-table
        title="已应用题目"
        :loading="loading"
        :visible="dialogVisibleInUse"
        rowKey="id"
        :columns="columns"
        :data-source="inUseList"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        @cancel="colseDialogInUse"
      >
      </a-table>

      <!-- 增加修改 -->
      <create-form ref="createForm" @ok="getList" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="id"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
      >
        <span slot="status" slot-scope="text, record">
          <a-popconfirm ok-text="是" cancel-text="否" @confirm="confirmHandleStatus(record)">
            <span slot="title"
              >确认<b>{{ record.status === '0' ? '启用' : '停用' }}</b
              >{{ record.issue }}题目吗?</span
            >
            <a-switch checked-children="开" un-checked-children="关" :checked="record.status == 0" />
          </a-popconfirm>
        </span>
        <span slot="type" slot-scope="text, record">
          {{ typeFormat(record.type) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a-divider type="vertical" v-hasPermi="['obj:issue:edit']" />
          <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['obj:issue:edit']">
            <a-icon type="edit" />修改
          </a>
          <!-- <a-divider type="vertical" v-hasPermi="['obj:issue:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['obj:issue:remove']">
            <a-icon type="delete" />删除
          </a> -->
        </span>
      </a-table>
      <!-- 分页 -->
      <a-pagination
        class="ant-table-pagination"
        show-size-changer
        show-quick-jumper
        :current="queryParam.pageNum"
        :total="total"
        :page-size="queryParam.pageSize"
        :showTotal="total => `共 ${total} 条`"
        @showSizeChange="onShowSizeChange"
        @change="changeSize"
      />
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { listIssue, delIssue, exportIssue, updateIssue, importIssue, downloadIssue } from '@/api/obj/issue'
import CreateForm from './modules/CreateForm'
import { getUploadUrl } from '@/utils/request'

export default {
  props: ['chapterId'],
  name: 'Issue',
  components: {
    CreateForm
  },
  data() {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      //状态字典
      statusOptions: [],
      //类型字典
      typeOptions: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
      inUseList: {},
      total: 0,
      // 查询参数
      queryParam: {
        chapterId: null,
        uuid: null,
        topicId: null,
        topicUuid: null,
        sn: null,
        issue: null,
        type: null,
        status: null,
        otherId: null,
        otherId1: null,
        otherId2: null,
        otherMsg: null,
        otherMsg1: null,
        otherMsg2: null,
        pageNum: 1,
        pageSize: 10
      },
      columns: [
        {
          title: '题目',
          dataIndex: 'issue',
          align: 'center'
        },
        {
          title: '类型',
          dataIndex: 'type',
          scopedSlots: { customRender: 'type' },
          ellipsis: true,
          align: 'center'
        },
        {
          title: '难易度',
          dataIndex: 'otherMsg1',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          ellipsis: true,
          align: 'center'
        },
        {
          title: '本题分数',
          dataIndex: 'otherMsg',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '备注',
          dataIndex: 'remark',
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          width: '18%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ],
      dialogVisibleIssue: false,
      dialogVisibleInUsel: false
    }
  },
  filters: {},
  created() {
    this.queryParam.chapterId = this.chapterId
    this.getList()
    this.getDicts('issue_status').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('issue_type').then(response => {
      this.typeOptions = response.data
    })
  },
  computed: {},
  watch: {},
  methods: {
    confirmHandleStatus(row) {
      const text = row.status === '0' ? '启用' : '关闭'
      row.status = row.status === '0' ? '1' : '0'
      updateIssue(row)
        .then(() => {
          this.$message.success(text + '成功', 3)
        })
        .catch(function() {
          this.$message.error(text + '异常', 3)
        })
    },
    //状态字典转译
    statusFormat(status) {
      return this.selectDictLabel(this.statusOptions, status)
    },
    //类型字典转译
    typeFormat(type) {
      return this.selectDictLabel(this.typeOptions, type)
    },
    /** 查询题库列表 */
    getList() {
      this.loading = true
      listIssue(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        chapterId: null,
        uuid: undefined,
        topicId: undefined,
        topicUuid: undefined,
        sn: undefined,
        issue: undefined,
        type: undefined,
        status: undefined,
        otherId: undefined,
        otherId1: undefined,
        otherId2: undefined,
        otherMsg: undefined,
        otherMsg1: undefined,
        otherMsg2: undefined,
        pageNum: 1,
        pageSize: 10
      }
      this.handleQuery()
    },
    onShowSizeChange(current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.queryParam.chapterId = this.chapterId
      this.getList()
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.queryParam.chapterId = this.chapterId
      this.getList()
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.id)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      var that = this
      const ids = row.id || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        //content: '当前选中编号为' + ids + '的数据',
        onOk() {
          return delIssue(ids).then(response => {
            if (response.code == 500) {
              that.inUseList = response.data
              that.dialogVisibleInUsel = true
              that.$message.success(response.msg, 3)
            }
            if (response.code == 200) {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success('删除成功', 3)
            }
          })
        },
        onCancel() {}
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk() {
          return exportIssue(that.queryParam).then(response => {
            that.download(response.msg)
            that.$message.success('导出成功', 3)
          })
        },
        onCancel() {}
      })
    },
    // 打开上传框
    openIssue() {
      this.dialogVisibleIssue = true
    },
    // 上传
    handleChange(info) {
      let formData = new FormData()
      formData.append('file', info)
      formData.append('chapterId', this.chapterId)
      importIssue(formData).then(response => {
        if (response.code === 200) {
          this.$message.success(info.name + ' 上传成功', 1.5)
        } else {
          this.$message.error(info.name + ' 上传失败', 1.5)
        }
        this.getList()
        this.colseDialogIssue()
      })
    },
    // 关闭上传框
    colseDialogIssue() {
      this.dialogVisibleIssue = false
    },
    // 关闭框
    colseDialogInUse() {
      this.dialogVisibleInUse = false
    },
    handleDownload() {
      downloadIssue().then(res => {
        if (res.code == 200) {
          this.download(res.data.fileName)
        }
      })
    }
  }
}
</script>

<style lang="less">
.fileupload {
  .ant-upload-list.ant-upload-list-text {
    display: none;
  }
}
</style>
