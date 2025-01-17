<template>
  <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item label="标题" prop="title" >
        <a-input v-model="form.title" placeholder="请输入标题" />
      </a-form-model-item>
      <a-form-model-item label="内容" prop="content" >
        <a-input v-model="form.content" placeholder="请输入内容" type="textarea" allow-clear />
      </a-form-model-item>
      <a-form-model-item label="图片" prop="imgUrl" >
         <file-upload v-model="form.imgUrl" type="image"></file-upload>
      </a-form-model-item>
      <a-form-model-item label="排序" prop="orderNum" >
        <a-input v-model="form.orderNum" placeholder="请输入状态" />
      </a-form-model-item>
      <a-form-model-item label="备注" prop="remark" >
        <a-input v-model="form.remark" placeholder="请输入内容" type="textarea" allow-clear />
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
import { getAdvert, addAdvert, updateAdvert } from '@/api/business/advert'

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
        id: null,
        title: null,
        content: null,
        status: 0,
        updateTime: null,
        createTime: null,
        createBy: null,
        url: null,
        imgUrl: null,
        orderNum: null,
        remark: null
      },
      // 1增加,2修改
      formType: 1,
      open: false,
      rules: {
        title: [
          { required: true, message: '标题不能为空', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '内容不能为空', trigger: 'blur' }
        ],
        imgUrl: [
          { required: true, message: '图片不能为空', trigger: 'blur' }
        ],
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
        id: null,
        title: null,
        content: null,
        status: 0,
        updateTime: null,
        createTime: null,
        createBy: null,
        url: null,
        imgUrl: null,
        orderNum: null,
        remark: null
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
      const id = row ? row.id : ids
      getAdvert(id).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid&&!this.loading) {
          this.loading=true
          if (this.form.id !== undefined && this.form.id !== null) {
            updateAdvert(this.form).then(response => {
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
            addAdvert(this.form).then(response => {
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
