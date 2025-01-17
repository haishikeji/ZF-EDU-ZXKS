<template>
  <!-- 增加修改 -->
  <div>
    <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" v-if="open" @close="cancel" :afterVisibleChange="afterVisibleChange">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
          <a-form-model-item label="姓名" prop="nickName">
            <a-input v-model="form.nickName" :maxLength="20"  placeholder="请输入" />
          </a-form-model-item>
           <a-form-model-item label="部门" prop="deptId">
              <a-tree-select
                v-model="form.deptId"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="deptOptions"
                placeholder="请选择"
                :replaceFields="replaceFields"
                tree-default-expand-all
              >
              </a-tree-select>
            </a-form-model-item>
          <a-form-model-item label="手机号" prop="phonenumber" >
            <a-input v-model="form.phonenumber" :maxLength="11" placeholder="请输入" :disabled="formTitle == '新增用户' ? false : true " />
          </a-form-model-item>
          <a-form-model-item label="学历" prop="education">
             <a-select placeholder="请选择学历" v-model="form.education" style="width: 100%" allow-clear>
                <a-select-option v-for="(d, index) in educationOptions" :key="index" :value="d.dictValue">{{ d.dictLabel }}</a-select-option>
              </a-select>
          </a-form-model-item>
          <a-form-model-item label="出生年月" prop="birth" >
            <a-month-picker
                v-model="form.birth"
                valueFormat="YYYY-MM"
                style="width:50%"
            />
          </a-form-model-item>
          <a-form-model-item  label="单位名称" prop="company">
            <a-input v-model="form.company" placeholder="请输入单位名称" :maxLength="128"/>
          </a-form-model-item>
          
          <a-form-model-item  label="性别" prop="sex">
            <a-radio-group v-model="form.sex" button-style="solid">
              <a-radio-button v-for="(d, index) in sexOptions" :key="index" :value="d.dictValue">{{ d.dictLabel }}</a-radio-button>
            </a-radio-group>
          </a-form-model-item>
          <a-form-model-item label="状态" prop="status">
            <a-radio-group v-model="form.status" button-style="solid" >
              <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{ d.dictLabel }}</a-radio-button>
            </a-radio-group>
          </a-form-model-item>
          <a-form-model-item label="备注" prop="remark">
            <a-input v-model="form.remark" placeholder="请输入" :maxLength="500" type="textarea" allow-clear />
          </a-form-model-item>
          <a-form-model-item label="密码" prop="password" v-if="form.userId == undefined">
            <a-input-password v-model="form.password" placeholder="请输入密码" :maxLength="100"/>
          </a-form-model-item>
          <!-- <a-form-model-item label="岗位" prop="postIds">
          <a-select
            mode="multiple"
            v-model="form.postIds"
            placeholder="请选择"
          >
            <a-select-option v-for="(d, index) in postOptions" :key="index" :value="d.postId">
              {{ d.postName }}
            </a-select-option>
          </a-select>
        </a-form-model-item> -->
        <a-form-model-item label="角色" prop="roleIds">
          <a-select
            mode="multiple"
            v-model="form.roleIds"
            placeholder="请选择"
          >
            <a-select-option v-for="(d, index) in roleOptions" :key="index" :value="d.roleId">
              {{ d.roleName }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" @click="submitForm" :loading="loading" >
            保存
          </a-button>
          <a-button type="dashed" @click="cancel">
            取消
          </a-button>
        </a-space>
      </div>
    </a-form-model>
  </a-drawer>
  <a-modal :visible="open1" width="50%" @cancel="onClose" :footer="null">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-row :gutter="24">
        <a-col :span="12">
          <a-form-model-item  prop="nickName">
            <label for="">姓名：{{form.nickName}}</label>
          </a-form-model-item>
        </a-col>
         <a-col :span="12">
          <a-form-model-item  prop="education">
            <label for="">学历：{{typeFormat1(form.education)}}</label>
          </a-form-model-item>
        </a-col>
        <a-col :span='12'>
          <a-form-model-item  prop="phonenumber">
            <label for="">手机号：{{form.phonenumber}}</label>
          </a-form-model-item>
        </a-col>
        <a-col :span='12'>
          <a-form-model-item  prop="birth">
            <label for="">出生年月：{{form.birth}}</label>
          </a-form-model-item>
        </a-col>
        <a-col :span='12'>
          <a-form-model-item  prop="company">
            <label for="">单位名称：{{form.company}}</label>
          </a-form-model-item>
        </a-col>
        <!-- <a-col :span='12'>
          <a-form-model-item a-form-model-item prop="email">
            <label for="">邮箱：{{form.email}}</label>
          </a-form-model-item>
        </a-col>
        <a-col :span='12' v-if="form.userId == undefined">
          <a-form-model-item  prop="userName" v-if="form.userId == undefined">
            <label for="">用户账号：{{form.userName}}</label>
          </a-form-model-item>
        </a-col> -->
        <a-col :span="12">
          <a-form-model-item a-form-model-item prop="sex">
            <label for="">性别：{{sexOptions[Number(form.sex)] && sexOptions[Number(form.sex)].dictLabel}}</label>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item prop="status">
            <label for="">状态： {{statusOptions[form.status] && statusOptions[form.status].dictLabel}}</label>
          </a-form-model-item>
        </a-col>
       <a-col :span="24">
          <a-form-model-item label="角色" prop="roleIds">
            <a-select
              mode="multiple"
              v-model="form.roleIds"
              placeholder="请选择"
              disabled
            >
              <a-select-option v-for="(d, index) in roleOptions" :key="index" :value="d.roleId">
                {{ d.roleName }}
              </a-select-option>
            </a-select>
          </a-form-model-item> 
        </a-col>
      <a-col :span="24">
        <label for="">备注：{{form.remark}}</label>
      </a-col>
      </a-row>
      <!-- <a-form-model-item label="密码" prop="password" v-if="form.userId == undefined">
        <a-input-password v-model="form.password" placeholder="请输入" :maxLength="100"/>
      </a-form-model-item> -->
      
      
      <!-- <a-form-model-item label="岗位" prop="postIds">
        <a-select
          mode="multiple"
          v-model="form.postIds"
          placeholder="请选择"
        >
          <a-select-option v-for="(d, index) in postOptions" :key="index" :value="d.postId">
            {{ d.postName }}
          </a-select-option>
        </a-select>
      </a-form-model-item>-->
      
      <div class="bottom-control" v-if="formType != 3">
        <a-space>
          <a-button type="primary" @click="submitForm" :loading="loading" >
            保存
          </a-button>
          <a-button type="dashed" @click="cancel">
            取消
          </a-button>
        </a-space>
      </div>
    </a-form-model>
  </a-modal>
  </div>
  
</template>

<script>

import { getUser, addUser, updateUser } from '@/api/system/user'

export default {
  name: 'CreateForm',
  props: {
    deptOptions: {
      type: Array,
      required: true
    },
    statusOptions: {
      type: Array,
      required: true
    },
    sexOptions: {
      type: Array,
      required: true
    }
  },
  components: {
  },
  data () {
    return {
      deptName: "",
      replaceFields: { children: 'children', title: 'label', key: 'id', value: 'id' },
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 默认密码
      //initPassword: undefined,
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
        identity: undefined,
        education: undefined,
        email: undefined,
        status: '0',
        remark: undefined,
        postIds: [],
        roleIds: []
      },
      open: false,
      open1: false,
      educationOptions: [],
      identityOptions: [],
      rules: {
        // userName: [
        //   { required: true, message: '用户账号不能为空', trigger: 'blur' },
        //   { max: 32, message: '用户账号不能超过32个字符', trigger: 'blur' }
        // ],
        education: [
          { required: true, message: '学历不能为空', trigger: 'blur' }
        ],
        birth: [
          { required: true, message: '出生年月不能为空', trigger: 'blur' }
        ],
        company: [
          { required: true, message: '单位名称不能为空', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '姓名不能为空', trigger: 'blur' },
          { max: 32, message: '姓名不能超过32个字符', trigger: 'blur' },
           {
            pattern:/^[_\u0391-\uFFE5a-zA-Z0-9]+$/,
            message: '姓名不能含特殊字符',
            trigger: ['blur', 'change']
          }
        ],
        deptId: [
          { required: true, message: '部门不能为空', trigger: 'change' }
        ],
        email: [
          { max: 32, message: '邮箱不能超过32个字符', trigger: 'change' }
        ],
        remark: [
          { max: 200, message: '备注不能超过200个字符', trigger: 'change' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
           { max: 32, message: '密码不能超过32个字符', trigger: 'blur' }
        ],
        phonenumber: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请正确填写手机号',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  filters: {
  },
  created () {
    // this.getConfigKey('sys.user.initPassword').then(response => {
    //   this.initPassword = response.msg
    // })
    this.getDicts('sys_education_type').then(response => {
      this.educationOptions = response.data
    })
    this.getDicts('sys_identity_type').then(response => {
      this.identityOptions = response.data
    })
    
  },
  computed: {
  },
  watch: {
  },
  methods: {
    afterVisibleChange(){
      document.getElementsByClassName('ant-drawer-wrapper-body')[0].scrollTop = 0
    },
    typeFormat1(type){
      return this.selectDictLabel(this.educationOptions, type)
    },
    typeFormat2(type){
      return this.selectDictLabel(this.identityOptions, type)
    },
    onClose () {
      this.open1 = false
      this.reset();
    }, 
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset () {
      this.formType=1;
      this.loading=false
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        status: '0',
        remark: undefined,
        postIds: [],
        roleIds: []
      }
    },
     /** 新增按钮操作 */
    handleAdd () {
      this.reset()
      this.formType=1;
      this.$emit('select-tree')
      getUser().then(response => {
        this.postOptions = response.posts
        let a = response.roles.filter(item => item.status == '0');
        this.roleOptions = a
        this.open = true
        this.formTitle = '新增用户'
        this.form.password = null
      })
    },
    /** 修改按钮操作 */
    handleUpdate (row, ids) {
      this.reset()
      this.formType=2;
      this.$emit('select-tree')
      const userId = row ? row.userId : ids
      getUser(userId).then(response => {
        this.form = response.data
        this.postOptions = response.posts
        let a = response.roles.filter(item => item.status == '0');
        this.roleOptions = a
        this.form.postIds = response.postIds
        this.form.roleIds = response.roleIds
        this.open = true
        this.formTitle = '修改用户'
        this.form.password = ''
      })
    },

    /** 查看按钮操作 */
    handleShow (row, ids) {
      this.reset()
      this.formType=3;
      this.$emit('select-tree')
      const userId = row ? row.userId : ids
      getUser(userId).then(response => {
        this.form = response.data
        this.postOptions = response.posts
        this.roleOptions = response.roles
        this.form.postIds = response.postIds
        this.form.roleIds = response.roleIds
        this.open1 = true
        this.formTitle = '查看用户'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.form.userName=this.form.phonenumber;
      this.$refs.form.validate(valid => {
        if (valid&&!this.loading) {
          this.loading = true;
          if (this.form.userId !== undefined) {
            updateUser(this.form).then(response => {
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
            addUser(this.form).then(response => {
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
