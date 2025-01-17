<template>
  <a-drawer width="40%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-form-model ref="form" :model="form" :rules="rules">
        <div v-if="form.answerList" style="width:100%;">
          <div v-for="(item,index) in form.answerList" :key="index" style="margin-bottom:20px;margin-left:6%;">
              <b>{{index+1}}.【{{typeFormat(item.objIssue.type)}}】{{item.objIssue.issue}}({{item.objIssue.otherMsg}}分)</b>
              <div v-if="item.objIssue.type == 'dx'">
                  <a-radio-group v-model="item.answerItemOptionIds[0]">
                    <div style="margin-top:20px;" v-for="(option,index) in item.objIssue.optionList" :key="index">
                        <a-radio disabled   :value="option.id">
                          {{option.option}}
                        </a-radio>
                    </div>
                  </a-radio-group>
                  <div style="margin-top:20px;"><b>正确解析：</b><b v-for="(d,ind) in item.objIssue.objOptions" :key="ind">{{d.option}} </b></div>
              </div>
              <div v-else-if="item.objIssue.type == 'dxs'">
                <a-checkbox-group  v-model="item.answerItemOptionIds">
                  <div style="margin-top:20px;" v-for="(option,index) in item.objIssue.optionList" :key="index">
                      <a-checkbox disabled  :value="option.id">
                        {{option.option}}
                      </a-checkbox>
                  </div>
                </a-checkbox-group>
                <div style="margin-top:20px;"><b>正确解析：</b><b v-for="(d,ind) in item.objIssue.objOptions" :key="ind">{{d.option}}&nbsp; </b></div>
              </div>
              <div v-else-if="item.objIssue.type == 'pd'">
                <a-checkbox-group  v-model="item.answerItemOptionIds">
                  <div style="margin-top:20px;" v-for="(option,index) in item.objIssue.optionList" :key="index">
                      <a-checkbox disabled  :value="option.id">
                        {{option.option}}
                      </a-checkbox>
                  </div>
                </a-checkbox-group>
                <div style="margin-top:20px;"><b>正确解析：</b><b v-for="(d,ind) in item.objIssue.objOptions" :key="ind">{{d.option}} </b></div>
              </div>
              <div v-else-if="item.objIssue.type == 'tk'">
                <div style="margin-top:20px;">
                  <b>作答：</b>
                  <b v-for="(answerItem,index) in item.answerItemList" :key="index">{{answerItem.answerContent}}</b>
                </div>
                <div style="margin-top:20px;"><b>正确解析：</b><b v-for="(d,ind) in item.objIssue.objOptions" :key="ind">{{d.option}}</b></div>
              </div>
          </div>
        </div>
    </a-form-model>
  </a-drawer>
</template>

<script>
  import { getIssueListItem } from '@/api/business/course'
  export default {
    name: 'viewForm',
    props: {},
    components: {},
    data() {
      return {
        loading: false,
        formTitle: '',
        //类型字典
      typeOptions:[],
        totalPoints:0,
        // 表单参数
        form: {
          id: null,
          uuid: null,
          topic: null,
          type: null,
          status: '0',
          remark: null,
          issueIds: []
        },
        // 1增加,2修改
        formType: 1,
        open: false,
        topicList: [],
        rules: {}
      }
    },
    filters: {},
    created() {
      this.getDicts('issue_type').then(response => {
        this.typeOptions = response.data
      });
    },
    computed: {},
    watch: {},
    mounted() {
    },
    methods: {
       //类型字典转译
      typeFormat(type){
        return this.selectDictLabel(this.typeOptions, type)
      },
      /** 预览按钮操作 */
      handleShow(row, totalPoints) {
        this.reset()
        getIssueListItem(row.chapterId,row.userId).then(response => {
          this.form = response.data
          this.open = true;
        })
      },
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
        this.totalPoints = 0
        this.form = {
          id: null,
          uuid: null,
          topic: null,
          type: null,
          status: '0',
          otherId: null,
          otherId1: null,
          otherId2: null,
          otherMsg: null,
          otherMsg1: null,
          otherMsg2: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          remark: null,
          issueIds:[]
        }
      },
    }
  }
</script>
<style scoped>
/deep/ .checkboxActive .ant-checkbox-checked .ant-checkbox-inner {
    background-color: red !important;
    border-color: #fff !important;
}
/deep/ .ant-checkbox-checked .ant-checkbox-inner {
    background-color: #1890ff !important;
    border-color: #fff !important;
}
/deep/ .radioActive .ant-radio-checked .ant-radio-inner {
  border-color: red !important;
}
/deep/ .radioActive .ant-radio-inner::after{
  background-color: red !important;
}
/deep/ .ant-checkbox-disabled.ant-checkbox-checked .ant-checkbox-inner::after {
   border-color: #fff !important;
}
/deep/.ant-radio-checked .ant-radio-inner {
  border-color: #1890ff !important;
}
/deep/ .ant-radio-inner::after{
  background-color: #1890ff !important;
}
/deep/ .ant-checkbox-wrapper-disabled span{
  color: black;
}
/deep/ .ant-radio-wrapper-disabled span {
  color: black;
}
</style>
