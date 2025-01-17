<template>
  <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item label="欢灯片标题" prop="title" >
        <a-input v-model="form.title" :maxLength="30" placeholder="请输入欢灯片标题" />
      </a-form-model-item>
      <a-form-model-item label="所属站点" prop="siteId" >
        <a-input v-model="form.siteId" :maxLength="20" placeholder="请输入所属站点" />
      </a-form-model-item>
      <a-form-model-item label="附件地址" prop="url" >
        <a-input v-model="form.url" placeholder="请输入内容" :maxLength="500" type="textarea" allow-clear />
      </a-form-model-item>
      <a-form-model-item label="是否置顶" prop="top" >
        <a-input v-model="form.top" placeholder="请输入是否置顶" />
      </a-form-model-item>
      <a-form-model-item label="欢灯片描述" prop="contentDescribe" >
        <a-input v-model="form.contentDescribe" placeholder="请输入欢灯片描述" />
      </a-form-model-item>
      <a-form-model-item label="类型:视频：video，图片:image" prop="contentType" >
        <a-select placeholder="请选择类型:视频：video，图片:image" v-model="form.contentType">
          <a-select-option value="" >请选择字典生成</a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="状态" prop="status" >
        <a-radio-group v-model="form.status" button-style="solid">
          <a-radio-button value="">请选择字典生成</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item label="删除标志" prop="delFlag" v-if="formType === 1">
        <a-input v-model="form.delFlag" placeholder="请输入删除标志" />
      </a-form-model-item>
      <a-form-model-item label="显示顺序" prop="orderNum" >
        <a-input v-model="form.orderNum" placeholder="请输入显示顺序" />
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" @click="submitForm" :loading="loading">
            保存
          </a-button>
          <a-button type="dashed" @click="cancel">
            取消
          </a-button>
        </a-space>
      </div>
    </a-form-model>
  </a-drawer>
</template>

<script>
import { getSlide, addSlide, updateSlide } from '@/api/cms/slide'

export default {
  name: 'CreateForm',
  props: {
  },
  components: {
  },
  data () {
    return {
      loading: false,
      formTitle: '',
      // 表单参数
      form: {
        slideId: null,
        title: null,
        siteId: null,
        url: null,
        top: null,
        contentDescribe: null,
        contentType: null,
        status: '0',
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        orderNum: null
      },
      // 1增加,2修改
      formType: 1,
      open: false,
      rules: {
      }
    }
  },
  filters: {
  },
  created () {
  },
  computed: {
  },
  watch: {
  },
  mounted () {
  },
  methods: {
    onClose () {
      this.open = false
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset () {
      this.formType = 1
      this.loading=false
      this.form = {
        slideId: null,
        title: null,
        siteId: null,
        url: null,
        top: null,
        contentDescribe: null,
        contentType: null,
        status: '0',
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        orderNum: null
      }
    },
    /** 新增按钮操作 */
    handleAdd (row) {
      this.reset()
      this.formType = 1
      this.open = true
      this.formTitle = '添加'
    },
    /** 修改按钮操作 */
    handleUpdate (row, ids) {
      this.reset()
      this.formType = 2
      const slideId = row ? row.slideId : ids
      getSlide(slideId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid&&!this.loading) {
          this.loading=true;
          if (this.form.slideId !== undefined && this.form.slideId !== null) {
            updateSlide(this.form).then(response => {
              this.$message.success(
                '修改成功',
                3
              )
              this.open = false
              this.$emit('ok')
            }).catch(()=> {
              this.loading = false;
            })
          } else {
            addSlide(this.form).then(response => {
              this.$message.success(
                '新增成功',
                3
              )
              this.open = false
              this.$emit('ok')
            }).catch(()=> {
              this.loading = false;
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
