<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="试卷名称" prop="topic">
                <a-input v-model="queryParam.topic" placeholder="请输入试卷名称" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <!-- <span class="table-page-search-submitButtons"
                    :style="advanced && { float: 'right', overflow: 'hidden' } || {} "> -->
                <a-button type="primary" @click="handleQuery"><a-icon type="search"/>查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo"/>重置</a-button>
                <!-- <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'"/>
                </a>
              </span> -->
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 增加修改 -->
      <create-form
        ref="createForm"
        @ok="getList"
      />
      <!-- 预览 -->
      <view-form
        ref="viewForm"
      />
      <!-- 数据展示 -->
      
      <a-tabs  type="card" default-active-key="nb" @change="callback">
        <a-tab-pane key="zs" tab="正式考">
          <!-- 操作 -->
          <div class="table-operations">
            <a-button type="primary" @click="$refs.createForm.handleAdd('zs')" v-hasPermi="['obj:topic:add']">
              <a-icon type="plus"/>
              新增
            </a-button>
            <a-button
              type="dashed"
              shape="circle"
              :loading="loading"
              :style="{float: 'right'}"
              icon="reload"
              @click="getList"/>
          </div>
          <a-table
            :loading="loading"
            :size="tableSize"
            rowKey="id"
            :columns="columns"
            :data-source="list"
            :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
            :pagination="false">
            <span slot="status" slot-scope="text, record">
              <a-popconfirm
                ok-text="是"
                cancel-text="否"
                @confirm="confirmHandleStatus(record)"
              >
                <span slot="title">确认<b>{{ record.status === '0' ? '启用' : '停用' }}</b>{{ record.issue }}题目吗?</span>
                <a-switch checked-children="开" un-checked-children="关" :checked="record.status == 0" />
              </a-popconfirm>
            </span>
            <span slot="operation" slot-scope="text, record">
              <a @click="$refs.viewForm.handleShow(record,undefined)">
                <a-icon type="eye" />预览
              </a>
              <a-divider type="vertical" v-hasPermi="['obj:topic:edit']"/>
              <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['obj:topic:edit']">
                <a-icon type="edit"/>修改
              </a>
              <a-divider type="vertical" v-hasPermi="['obj:topic:remove']"/>
              <a @click="handleDelete(record)" v-hasPermi="['obj:topic:remove']">
                <a-icon type="delete"/>删除
              </a>
            </span>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="mn" tab="模拟考">
          <!-- 操作 -->
          <div class="table-operations">
            <a-button type="primary" @click="$refs.createForm.handleAdd('mn')" v-hasPermi="['obj:topic:add']">
              <a-icon type="plus"/>
              新增
            </a-button>
            <a-button
              type="dashed"
              shape="circle"
              :loading="loading"
              :style="{float: 'right'}"
              icon="reload"
              @click="getList"/>
          </div>
          <a-table
            :loading="loading"
            :size="tableSize"
            rowKey="id"
            :columns="columns"
            :data-source="list"
            :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
            :pagination="false">
            <span slot="status" slot-scope="text, record">
              <a-popconfirm
                ok-text="是"
                cancel-text="否"
                @confirm="confirmHandleStatus(record)"
              >
                <span slot="title">确认<b>{{ record.status === '0' ? '启用' : '停用' }}</b>{{ record.issue }}题目吗?</span>
                <a-switch checked-children="开" un-checked-children="关" :checked="record.status == 0" />
              </a-popconfirm>
            </span>
            <span slot="operation" slot-scope="text, record">
              <a @click="$refs.viewForm.handleShow(record,undefined)">
                <a-icon type="eye" />预览
              </a>
              <a-divider type="vertical" v-hasPermi="['obj:topic:edit']"/>
              <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['obj:topic:edit']">
                <a-icon type="edit"/>修改
              </a>
              <a-divider type="vertical" v-hasPermi="['obj:topic:remove']"/>
              <a @click="handleDelete(record)" v-hasPermi="['obj:topic:remove']">
                <a-icon type="delete"/>删除
              </a>
            </span>
          </a-table>
        </a-tab-pane>
      </a-tabs>
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
  import { listTopic, delTopic, exportTopic, getIssueList,updateTopic } from '@/api/obj/topic'
  import CreateForm from './modules/CreateForm'
  import viewForm from './modules/viewForm'

  export default {
    name: 'Topic',
    components: {
      CreateForm,
      viewForm
    },
    data() {
      return {
        list: [],
        topicList: [],
        selectedRowKeys: [],
        selectedRows: [],
        //状态字典
        statusOptions:[],
        //考试类型字典
        typeOptions: [
          {
            dictLabel: "正式考",
            dictValue: "zs"
          },{
            dictLabel: "模拟考",
            dictValue: "mn"
          },
        ],
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
          topic: null,
          type: null,
          status: null,
          pageNum: 1,
          pageSize: 10
        },
        columns: [
          {
            title: '试卷名称',
            dataIndex: 'topic',
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
            title: '总分',
            dataIndex: 'otherId',
            ellipsis: true,
            align: 'center'
          },
          {
            title: '考试人数',
            dataIndex: 'examNum',
            ellipsis: true,
            align: 'center'
          },
          {
            title: '备注',
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
    filters: {},
    created() {
      this.getList()
      this.getTopicList()
      this.getDicts('topic_status').then(response => {
      this.statusOptions = response.data
    });
    },
    computed: {},
    watch: {},
    methods: {
      callback(key) {
        this.queryParam.type=key;
        this.queryParam.pageNum = 1;
        this.getList();
      },
      confirmHandleStatus(row){
        const text = row.status === '0' ? '启用' : '关闭'
        row.status = row.status === '0' ? '1' : '0'
        updateTopic(row).then(() => {
          this.$message.success(
            text + '成功',
            3
          )
        }).catch(function () {
          this.$message.error(
            text + '异常',
            3
          )
        });
      },
      //状态字典转译
    statusFormat(status){
       return this.selectDictLabel(this.statusOptions, status)
    },
      /** 查询题目列表 */
      getList() {
        this.loading = true
        listTopic(this.queryParam).then(response => {
          this.list = response.rows
          this.total = response.total
          this.loading = false
        })
      },
      getTopicList() {
        getIssueList().then(response => {
          this.topicList = response.rows
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
        uuid: undefined,
        topic: undefined,
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
      this.getList()
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
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
          return delTopic(ids)
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '删除成功',
                3
              )
            })
        },
        onCancel() {
        }
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk() {
          return exportTopic(that.queryParam)
            .then(response => {
              that.download(response.msg)
              that.$message.success(
                '导出成功',
                3
              )
            })
        },
        onCancel() {
        }
      })
    }
  }
  }
</script>
