<template>
  <!-- 增加修改 -->
  <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-row :gutter="24">
      <a-card :bordered="false">
        <a-row :gutter="24">
          <a-col :span="24">
            <a-descriptions title="基本信息" :column="{ xs: 2, sm: 2, md: 2}">
              <a-descriptions-item label="用户昵称" :span="2">
                {{form.nickName}}
              </a-descriptions-item>
              <a-descriptions-item label="部门" :span="2" >
                {{form.dept.deptName}}
              </a-descriptions-item>
              <a-descriptions-item label="手机号" :span="2">
                {{form.phonenumber}}
              </a-descriptions-item>
              <a-descriptions-item label="邮箱" :span="2">
                {{form.email}}
              </a-descriptions-item>
              <a-descriptions-item label="性别" :span="2" v-for="(d, index) in sexOptions" :key="index"  v-if="form.sex==d.dictValue">
              {{form.sex}} {{d.dictValue}}
                {{d.dictLabel}}
              </a-descriptions-item>
              <a-descriptions-item label="身份证正面" :span="2" v-if="form.idCardFrontImg">
                <span class="qwer">
                  <img class="qwertyuiop" @click="handlePreview(form.idCardFrontImg)" :src="form.idCardFrontImg" style="width: 245px;height: 159px;object-fit: cover;" />
                </span>
              </a-descriptions-item>
              <a-descriptions-item label="身份证反面" :span="2" v-if="form.idCardReverseImg">
                <span class="qwer">
                  <img class="qwertyuiop" @click="handlePreview(form.idCardReverseImg)" :src="form.idCardReverseImg" style="width: 245px;height: 159px;object-fit: cover;" />
                </span>
              </a-descriptions-item>
              <a-descriptions-item label="人脸图片" :span="2" v-if="form.faceImg">
                <span class="qwer">
                  <img class="qwertyuiop" @click="handlePreview(form.faceImg)" :src="form.faceImg" style="width: 245px;height: 159px;object-fit: cover;" />
                </span>
              </a-descriptions-item>
            </a-descriptions>
          </a-col>
        </a-row>
      </a-card>
    </a-row>
  </a-drawer>
</template>

<script>
export default {
  name: 'InfoForm',
  props: {
  },
  components: {
  },
  data () {
    return {
      replaceFields: { children: 'children', title: 'label', key: 'id', value: 'id' },
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 默认密码
      initPassword: undefined,
      sexOptions:[],
      deptOptions:[],
      formTitle: '',
      // 1增加,2修改,3查看
      formType: 1,
      loading:false,
      // 表单参数
      form: {
        userId: undefined,
        deptId: 0,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: '3',
        status: '0',
        remark: undefined,
        postIds: [],
        roleIds: []
      },
      open: false,
    }
  },
  filters: {
  },
  created () {
    this.getDicts('sys_user_sex').then(response => {
      this.sexOptions = response.data
    })
  },
  computed: {
  },
  watch: {
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
      this.formType=1;
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: '3',
        status: '0',
        remark: undefined,
        postIds: [],
        roleIds: []
      }
    },
    
    /** 查看按钮操作 */
    handleShow (row) {
      this.reset()
      this.form = row
      this.open = true
    },
    
  }
}
</script>
