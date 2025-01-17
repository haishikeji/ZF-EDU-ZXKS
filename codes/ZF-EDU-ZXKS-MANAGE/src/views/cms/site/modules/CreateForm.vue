<template>
  <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <!-- <a-form-model-item label="上级站点" :prop="siteOptions.length>0?'parentId':''">
        <a-tree-select
          v-model="form.parentId"
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          :tree-data="siteOptions"
          placeholder="请选择"
          :replaceFields="{children:'children', title:'siteName', key:'siteId', value: 'siteId' }"
          tree-default-expand-all
        >
        </a-tree-select>
      </a-form-model-item> -->
      <a-form-model-item label="站点名称" prop="siteName">
        <a-input v-model="form.siteName" :maxLength="30" placeholder="请输入站群名称" />
      </a-form-model-item>
      <a-form-model-item label="站点logo" prop="siteLogo">
        <a-upload
          name="avatar"
          list-type="picture-card"
          class="avatar-uploader"
          :show-upload-list="false"
          :action="getUploadUrl"
          :before-upload="beforeUpload"
        >
          <img v-if="form.siteLogo" :src="form.siteLogo" alt="avatar" class="avatar" />
          <div v-else>
            <a-icon :type="imgLoading ? 'loading' : 'plus'" />
            <div class="ant-upload-text">上传</div>
          </div>
        </a-upload>
      </a-form-model-item>
      <a-form-model-item label="站点URL" prop="siteUrl">
        <a-input v-model="form.siteUrl" :maxLength="100" placeholder="请输入站点URL" />
      </a-form-model-item>
      <a-form-model-item label="站点关键字" prop="siteKeyword">
        <a-input v-model="form.siteKeyword" :maxLength="200" placeholder="请输入站点关键字" />
      </a-form-model-item>
      <a-form-model-item label="站点描述" prop="siteDescribe">
        <a-input v-model="form.siteDescribe" placeholder="请输入站点描述" :maxLength="300" type="textarea" />
      </a-form-model-item>
      <!-- <a-form-model-item label="所属组织" prop="deptId" >
         <a-tree-select
          v-model="form.deptId"
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          :tree-data="deptOptions"
          placeholder="请选择"
          :replaceFields="{children:'children', title:'deptName', key:'deptId', value: 'deptId' }"
          tree-default-expand-all
        >
        </a-tree-select>
      </a-form-model-item> -->
      <a-form-model-item label="显示顺序" prop="orderNum">
        <a-input-number v-model="form.orderNum" :min="1" :max="10000" style="width: 100%" />
      </a-form-model-item>
      <a-form-model-item label="站点状态" prop="status">
        <a-radio-group v-model="form.status" button-style="solid">
          <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
            d.dictLabel
          }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-divider>站点配置</a-divider>
      <a-form-model-item label="站点模板" prop="siteTpl">
        <a-input v-model="form.siteTpl" placeholder="请输入站点模板 如：/jypt" :maxLength="300" />
      </a-form-model-item>
      <a-form-model-item label="站点主题" prop="siteTheme">
        <div style="height: 20px">
          <a-tooltip class="setting-drawer-theme-color-colorBlock" v-for="(item, index) in colorList" :key="index">
            <template slot="title">
              {{ item.key }}
            </template>
            <a-tag :color="item.color" @click="changeColor(item.color)">
              <a-icon type="check" v-if="item.color === form.siteTheme"></a-icon>
            </a-tag>
          </a-tooltip>
        </div>
      </a-form-model-item>
      <a-form-model-item label="静态资源路径" prop="siteRecPath">
        <a-input v-model="form.siteRecPath" :maxLength="100" placeholder="请输入静态资源路径" />
      </a-form-model-item>
      <a-form-model-item label="备案ICP" prop="siteIcp">
        <a-input v-model="form.siteIcp" placeholder="请输入备案ICP 如：豫ICP证xxxxxxxx号" :maxLength="100" />
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" @click="submitForm" :loading="loading"> 保存 </a-button>
          <a-button type="dashed" @click="cancel"> 取消 </a-button>
        </a-space>
      </div>
    </a-form-model>
  </a-drawer>
</template>

<script>
import { getSite, addSite, updateSite } from '@/api/cms/site'
import { listDept } from '@/api/system/dept'
import { uploadImg } from '@/api/common'
import { getUploadUrl } from '@/utils/request'
export default {
  name: 'CreateForm',
  props: {
    statusOptions: {
      type: Array,
      required: true
    },
    siteOptions: {
      type: Array,
      required: true
    }
  },
  components: {},
  data() {
    return {
      loading: false,
      imgLoading: false,
      formTitle: '',
      // 表单参数
      form: {
        siteId: null,
        parentId: null,
        ancestors: null,
        siteName: null,
        orderNum: null,
        userId: null,
        siteUrl: null,
        siteLogo: null,
        siteKeyword: null,
        siteDescribe: null,
        siteTpl: null,
        siteTheme: null,
        siteRecPath: null,
        siteIcp: null,
        status: '0',
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      },
      colorList: [
        {
          key: '薄暮',
          color: '#F5222D'
        },
        {
          key: '火山',
          color: '#FA541C'
        },
        {
          key: '日暮',
          color: '#FAAD14'
        },
        {
          key: '明青',
          color: '#13C2C2'
        },
        {
          key: '极光绿',
          color: '#52C41A'
        },
        {
          key: '拂晓蓝（默认）',
          color: '#1890FF'
        },
        {
          key: '极客蓝',
          color: '#2F54EB'
        },
        {
          key: '酱紫',
          color: '#722ED1'
        }
      ],
      formType: 1,
      deptOptions: [],
      open: false,
      rules: {
        parentId: [{ required: true, message: '上级站点不能为空', trigger: 'blur' }],
        siteName: [{ required: true, message: '站点名称不能为空', trigger: 'blur' }],
        siteUrl: [{ required: true, message: '站点域名不能为空', trigger: 'blur' }]
      }
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  mounted() {},
  methods: {
    onClose() {
      this.open = false
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.formType = 1
      this.loading = false
      this.form = {
        siteId: null,
        parentId: null,
        ancestors: null,
        siteName: null,
        orderNum: null,
        userId: null,
        siteUrl: null,
        siteLogo: null,
        siteKeyword: null,
        siteDescribe: null,
        siteTpl: null,
        siteTheme: null,
        siteRecPath: null,
        siteIcp: null,
        status: '0',
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      }
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      if (row !== undefined) {
        this.form.parentId = row.siteId
      }
      this.formType = 1
      this.open = true
      this.formTitle = '添加'
      this.$emit('select-tree')
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, 'deptId')
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      this.reset()
      this.formType = 2
      const siteId = row ? row.siteId : ids
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, 'deptId')
      })
      getSite(siteId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改'
      })
    },
    changeColor(color) {
      if (this.form.siteTheme !== color) {
        this.form.siteTheme = color
      }
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
          this.form.siteLogo = response.url
          this.$message.info('上传成功')
        } else {
          this.msgError(response.msg)
        }
        this.imgLoading = false
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs.form.validate(valid => {
        if (valid && !this.loading) {
          this.loading = true
          if (this.form.siteId !== undefined && this.form.siteId !== null) {
            updateSite(this.form)
              .then(response => {
                this.$message.success('修改成功', 3)
                this.open = false
                this.$emit('ok')
              })
              .catch(() => {
                this.loading = false
              })
          } else {
            addSite(this.form)
              .then(response => {
                this.$message.success('新增成功', 3)
                this.open = false
                this.$emit('ok')
              })
              .catch(() => {
                this.loading = false
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
<style>
.avatar-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}
.avatar-uploader .avatar {
  width: 128px;
  height: 128px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
<style lang="less" scoped>
.setting-drawer-theme-color-colorBlock {
  width: 20px;
  height: 20px;
  border-radius: 2px;
  float: left;
  cursor: pointer;
  margin-right: 8px;
  padding-left: 0px;
  padding-right: 0px;
  text-align: center;
  color: #fff;
  font-weight: 700;

  i {
    font-size: 14px;
  }
}
</style>
