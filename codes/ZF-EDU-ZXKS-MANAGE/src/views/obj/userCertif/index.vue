<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="鐘舵€?" prop="uuid">
                <a-input v-model="queryParam.uuid" placeholder="请输入鐘舵€?" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="用户id" prop="userId">
                <a-input v-model="queryParam.userId" placeholder="请输入用户id" allow-clear/>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="证书id" prop="cerefiId">
                  <a-input v-model="queryParam.cerefiId" placeholder="请输入证书id" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="澶囩敤id" prop="otherId">
                  <a-input v-model="queryParam.otherId" placeholder="请输入澶囩敤id" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="澶囩敤1" prop="otherId1">
                  <a-input v-model="queryParam.otherId1" placeholder="请输入澶囩敤1" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="澶囩敤2" prop="otherId2">
                  <a-input v-model="queryParam.otherId2" placeholder="请输入澶囩敤2" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="澶囩敤" prop="otherMsg">
                  <a-input v-model="queryParam.otherMsg" placeholder="请输入澶囩敤" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="澶囩敤1" prop="otherMsg1">
                  <a-input v-model="queryParam.otherMsg1" placeholder="请输入澶囩敤1" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="澶囩敤2" prop="otherMsg2">
                  <a-input v-model="queryParam.otherMsg2" placeholder="请输入澶囩敤2" allow-clear/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="鐘舵€?" prop="status">
                  <a-select placeholder="请选择鐘舵€?" v-model="queryParam.status" style="width: 100%" allow-clear>
                    <a-select-option>请选择字典生成</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </template>
            <a-col :md="!advanced && 8 || 24" :sm="24">
              <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
                <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'"/>
                </a>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 操作 -->
      <div class="table-operations">
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['obj:certif:add']">
          <a-icon type="plus" />新增
        </a-button>
        <a-button type="primary" :disabled="single" @click="$refs.createForm.handleUpdate(undefined, ids)" v-hasPermi="['obj:certif:edit']">
          <a-icon type="edit" />修改
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['obj:certif:remove']">
          <a-icon type="delete" />删除
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['obj:certif:export']">
          <a-icon type="download" />导出
        </a-button>
        <a-button
          type="dashed"
          shape="circle"
          :loading="loading"
          :style="{float: 'right'}"
          icon="reload"
          @click="getList" />
      </div>
      <!-- 增加修改 -->
      <create-form
        ref="createForm"
        @ok="getList"
      />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="id"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false">
        <span slot="operation" slot-scope="text, record">
          <a-divider type="vertical" v-hasPermi="['obj:certif:edit']" />
          <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['obj:certif:edit']">
            <a-icon type="edit" />修改
          </a>
          <a-divider type="vertical" v-hasPermi="['obj:certif:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['obj:certif:remove']">
            <a-icon type="delete" />删除
          </a>
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
import { listCertif, delCertif, exportCertif } from '@/api/obj/userCertif'
import CreateForm from './modules/CreateForm'

export default {
  name: 'Certif',
  components: {
    CreateForm
  },
  data () {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
      total: 0,
      // 查询参数
      queryParam: {
        uuid: null,
        userId: null,
        cerefiId: null,
        otherId: null,
        otherId1: null,
        otherId2: null,
        otherMsg: null,
        otherMsg1: null,
        otherMsg2: null,
        status: null,
        pageNum: 1,
        pageSize: 10
      },
      columns: [
        {
          title: '澶囨敞',
          dataIndex: 'id',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '澶囨敞',
          dataIndex: 'uuid',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '用户id',
          dataIndex: 'userId',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '证书id',
          dataIndex: 'cerefiId',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '澶囩敤id',
          dataIndex: 'otherId',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '澶囩敤1',
          dataIndex: 'otherId1',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '澶囩敤2',
          dataIndex: 'otherId2',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '澶囩敤',
          dataIndex: 'otherMsg',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '澶囩敤1',
          dataIndex: 'otherMsg1',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '澶囩敤2',
          dataIndex: 'otherMsg2',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '鐘舵€?',
          dataIndex: 'status',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '澶囨敞',
          dataIndex: 'remark',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          width: '18%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {
  },
  created () {
    this.getList()
  },
  computed: {
  },
  watch: {
  },
  methods: {
    /** 查询用户关联证书列表 */
    getList () {
      this.loading = true
      listCertif(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParam = {
        uuid: undefined,
        userId: undefined,
        cerefiId: undefined,
        otherId: undefined,
        otherId1: undefined,
        otherId2: undefined,
        otherMsg: undefined,
        otherMsg1: undefined,
        otherMsg2: undefined,
        status: undefined,
        pageNum: 1,
        pageSize: 10
      }
      this.handleQuery()
    },
    onShowSizeChange (current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    changeSize (current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.id)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      var that = this
      const ids = row.id || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        //content: '当前选中编号为' + ids + '的数据',
        onOk () {
          return delCertif(ids)
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '删除成功',
                3
              )
          })
        },
        onCancel () {}
      })
    },
    /** 导出按钮操作 */
    handleExport () {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk () {
          return exportCertif(that.queryParam)
            .then(response => {
              that.download(response.msg)
              that.$message.success(
                '导出成功',
                3
              )
          })
        },
        onCancel () {}
      })
    }
  }
}
</script>
