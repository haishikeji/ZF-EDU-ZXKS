<template>
  <page-header-wrapper class="xxb-index">
      <a-card :bordered="false">
              <div class="table-page-search-wrapper">
                    <a-form layout="inline">
                      <a-row :gutter="48">
                        <a-col :md="8" :sm="24">
                          <a-form-item label="学习班名称" prop="title">
                            <a-input v-model="queryParam.title" :maxLength="20" placeholder="请输入学习班名称" allow-clear/>
                          </a-form-item>
                        </a-col>
                        <a-col :md="!advanced && 8 || 24" :sm="24">
                          <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                            <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                            <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
                            <!-- <a @click="toggleAdvanced" style="margin-left: 8px">
                              {{ advanced ? '收起' : '展开' }}
                              <a-icon :type="advanced ? 'up' : 'down'"/>
                            </a> -->
                          </span>
                        </a-col>
                      </a-row>
                    </a-form>
              </div>
              <a-row :gutter="25">
                  <a-col :span="5" v-for="item in list" :key="item.studyId">
                      <a-card hoverable style="width: auto;margin-bottom:20px;">
                          <img
                            slot="cover"
                            alt="example"
                            :src="item.coverImg"
                            style="width:100%;height:180px;"
                          />
                          <template slot="actions" class="ant-card-actions">
                            <a-tag color="orange"  v-if="item.status=='wfb'">
                              未发布
                            </a-tag>
                            <a-tag color="volcano"  v-else-if="item.status=='shz'">
                              待审核
                            </a-tag>
                            <a-tag color="green" v-else-if="item.status=='yfb'">
                              已发布
                            </a-tag>
                            <a-tag color="red" v-else-if="item.status=='ygb'">
                              已关闭
                            </a-tag>
                            <a-tag color="red" v-else-if="item.status=='ybh'">
                              已驳回
                            </a-tag>
                            <a-tooltip placement="topLeft" title="统计查看" arrow-point-at-center>
                                <a-icon key="eye" type="eye" @click="toView(item.studyId)"/>
                            </a-tooltip>
                          </template>
                          <a-card-meta :title="item.title" :description="item.info">
                          </a-card-meta>
                      </a-card>
                  </a-col>
              </a-row>
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
      <user-import ref="userInfo"/>
  </page-header-wrapper>
</template>
<script>
import { trackList} from '@/api/business/study'
import userImport from "./modules/user";
export default {
  name: 'studyTrack',
  components: {
    userImport
  },
  created () {
    this.getList();
    this.getDicts('app_course_status').then(response => {
      this.statusOptions = response.data
    })
  },
  computed: {
  },
  watch: {

  },
  data () {
    return {
        statusOptions:[],
        advanced: false,
        dateRange: [],
        ids:[],
        queryParam: {
          bdKeys:[],
          title: null,
          teacherId: null,
          type: null,
          status: 'yfb',
          orderByColumn:'createTime',
          isAsc:'desc',
          pageNum: 1,
          pageSize: 10
        },
        ids:[],
        total: 0,
        list:[]
    }
  },
  methods: {
    getList(){
        trackList(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
          this.list = response.rows
          this.total = response.total
          this.loading = false
        })
    },
    onCheck(checkedKeys) {
      this.queryParam.typeIds = checkedKeys;
      this.queryParam.pageNum = 1;
      this.getList();
    },
    
    handleQuery () {
      this.queryParam.pageNum = 1
      this.getList();
    },
    
    onShowSizeChange (current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    resetQuery () {
      this.dateRange = []
      this.queryParam = {
        title: null,
        teacherId: undefined,
        type: undefined,
        top: undefined,
        status: "yfb",
        orderNum: undefined,
        pageNum: 1,
        pageSize: 10
      }
      this.handleQuery();
    },
    handleQuery () {
      this.queryParam.pageNum = 1
      this.getList();
    },
    changeSize (current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    
    cancelHandleStatus (row) {
    },
    toView(studyId){
      this.$refs.userInfo.show(studyId);
    },
    
  }
}
</script>
<style lang="less">
.xxb-index{
  .ant-card-meta-description{
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
    width:200px;
  }
}
</style>