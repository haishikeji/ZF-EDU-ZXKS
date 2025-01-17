<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <a-row :gutter="24">
        <a-col :span="4">
          <!-- 部门树 -->
          <channel-tree ref="deptTree" :channelOptions="channelOptions" @select="clickChannelNode" />
        </a-col>
        <a-col :span="20" style="min-height: 610px">
          <!-- 操作 -->
          <div class="table-operations">
            <a-button type="primary" @click="$refs.createForm.handleAdd()"> <a-icon type="plus" />新增 </a-button>
            <a-button type="danger" @click="handleDelete"> <a-icon type="delete" />删除 </a-button>
          </div>
          <create-form
            ref="createForm"
            :channelOptions="channelOptions"
            :typeOptions="typeOptions"
            @select-tree="getTreeselect"
            @ok="getTreeselect"
          />
          <a-form-model
            ref="formRef"
            labelAlign="right"
            layout="inline"
            :model="form"
            class="form-row-channal"
            :rules="rules"
          >
            <a-tabs default-active-key="1">
              <a-tab-pane key="1" tab="基本信息">
                <a-row>
                  <a-col :span="24">
                    <a-form-model-item label="栏目ID">
                      {{ form.channelId }}
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="栏目模型" prop="channelType">
                      <a-select placeholder="请选择" v-model="form.channelType" style="width: 400px" allow-clear>
                        <a-select-option v-for="(d, index) in typeOptions" :key="index" :value="d.dictValue">{{
                          d.dictLabel
                        }}</a-select-option>
                      </a-select>
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="上级栏目" prop="parentId">
                      <a-tree-select
                        v-model="form.parentId"
                        style="width: 400px"
                        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                        :tree-data="channelOptions"
                        placeholder="请选择"
                        :replaceFields="replaceFields"
                        tree-default-expand-all
                      >
                      </a-tree-select>
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="栏目名称" prop="channelName">
                      <a-input
                        v-model="form.channelName"
                        :maxLength="30"
                        placeholder="请输入栏目名称"
                        style="width: 400px"
                      />
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="栏目别名">
                      <a-input
                        v-model="form.channelCode"
                        :maxLength="30"
                        placeholder="请输入栏目别名"
                        style="width: 400px"
                      />
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="是否导航">
                      <a-radio-group v-model="form.isNav">
                        <a-radio :value="0">是</a-radio>
                        <a-radio :value="1">否</a-radio>
                      </a-radio-group>
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="显示顺序">
                      <a-input-number v-model="form.orderNum" :min="1" :max="100" style="width: 400px" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="栏目状态">
                      <a-radio-group v-model="form.status" button-style="solid">
                        <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
                          d.dictLabel
                        }}</a-radio-button>
                      </a-radio-group>
                    </a-form-model-item>
                  </a-col>
                </a-row>
              </a-tab-pane>
              <a-tab-pane key="2" tab="扩展信息" force-render>
                <a-row>
                  <a-col :span="24">
                    <a-form-model-item label="栏目图片">
                      <a-upload
                        name="avatar"
                        list-type="picture-card"
                        class="avatar-uploader"
                        :show-upload-list="false"
                        :action="getUploadUrl"
                        :before-upload="beforeUpload"
                      >
                        <img v-if="form.channelImg" :src="form.channelImg" alt="avatar" class="avatar" />
                        <div v-else>
                          <a-icon :type="imgLoading ? 'loading' : 'plus'" />
                          <div class="ant-upload-text">上传</div>
                        </div>
                      </a-upload>
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="栏目描述">
                      <a-input
                        v-model="form.channelDescribe"
                        placeholder="请输入栏目描述"
                        type="textarea"
                        style="width: 400px"
                      />
                    </a-form-model-item>
                  </a-col>
                </a-row>
              </a-tab-pane>
            </a-tabs>
          </a-form-model>
          <div class="bottom-control" style="top: 570px">
            <a-space style="float: left; margin-left: 80px">
              <a-button type="primary" @click="submitForm"> 保存 </a-button>
              <!-- <a-button type="dashed">
                重置
              </a-button> -->
            </a-space>
          </div>
        </a-col>
      </a-row>
    </a-card>
  </page-header-wrapper>
</template>
<script>
import { delChannel, getChannel, treeselect, updateChannel } from '@/api/cms/channel'
import CreateForm from './modules/CreateForm'
import ChannelTree from './modules/ChannelTree'
import { uploadImg } from '@/api/common'
import { getUploadUrl } from '@/utils/request'

export default {
  name: 'Channel',
  components: {
    CreateForm,
    ChannelTree
  },
  data() {
    return {
      replaceFields: { children: 'children', title: 'label', key: 'id', value: 'id' },
      list: [],
      form: {
        isNav: 0
      },
      imgLoading: false,
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: true,
      total: 0,
      // 状态数据字典
      statusOptions: [],
      channelOptions: [
        {
          id: 0,
          label: '',
          children: []
        }
      ],
      // 日期范围
      dateRange: [],
      typeOptions: [],
      queryParam: {
        status: undefined,
        channelId: undefined
      },
      rules: {
        parentId: [{ required: true, message: '上级栏目不能为空', trigger: 'blur' }],
        channelName: [{ required: true, message: '栏目名称不能为空', trigger: 'blur' }],
        channelType: [{ required: true, message: '栏目模型不能为空', trigger: 'blur' }]
      }
    }
  },
  filters: {},
  created() {
    this.getTreeselect()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('app_channel_type').then(response => {
      this.typeOptions = response.data
    })
  },
  computed: {},
  watch: {},
  methods: {
    getInfo(channelId) {
      getChannel(channelId).then(response => {
        this.form = response.data
      })
    },
    getTreeselect() {
      treeselect({ type: 'zj' }).then(response => {
        let channelOptions = new Array()
        channelOptions.push({
          id: 0,
          label: '豫青学',
          children: response.data
        })
        this.channelOptions = channelOptions
        this.getInfo(response.data[0].id)
      })
    },
    resetQuery() {
      this.dateRange = []
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      }
    },
    clickChannelNode(channelId) {
      if (channelId != 0) {
        this.getInfo(channelId)
      }
    },
    handleDelete() {
      var that = this
      const channelIds = this.form.channelId || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        //content: '当前选中栏目为 ‘' + this.form.channelName + '’ 的数据',
        onOk() {
          return delChannel(channelIds).then(() => {
            that.getTreeselect()
            that.$message.success('删除成功', 3)
          })
        },
        onCancel() {}
      })
    },
    beforeUpload(file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
      if (!isJpgOrPng) {
        this.$message.error('请上传png或者jpg!')
        return false
      }
      const isLt5M = file.size / 1024 / 1024 < 10
      if (!isLt5M) {
        this.$message.error('上传文件不能超过 10MB!')
        return false
      }
      this.imgLoading = true
      let formData = new FormData()
      formData.append('file', file)
      uploadImg(formData).then(response => {
        if (response.code === 200) {
          this.form.channelImg = response.url
          this.$message.info('上传成功')
        } else {
          this.msgError(response.msg)
        }
        this.imgLoading = false
      })
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (valid) {
          if (this.form.channelId !== undefined && this.form.channelId !== null) {
            updateChannel(this.form).then(response => {
              this.$message.success('修改成功', 3)
              this.getTreeselect()
            })
          } else {
            addChannel(this.form).then(response => {
              this.$message.success('新增成功', 3)
            })
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
.form-row-channal {
  .ant-row {
    .ant-col {
      margin: 15px 10px 0px;
      /deep/ .ant-form-item-label {
        width: 150px;
        text-align: right;
      }
    }
  }
  /deep/ label {
    font-size: 16px;
  }
}
.avatar-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}
.avatar-uploader .avatar {
  width: 128px;
  height: 128px;
}
</style>
