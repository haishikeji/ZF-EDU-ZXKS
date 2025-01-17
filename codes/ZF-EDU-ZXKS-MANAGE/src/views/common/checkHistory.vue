<template>
    <a-modal
      title="审核历史"
      :visible="dialogVisibleHis"
      :width="800"
      :footer="null"
      @cancel ="colseDialogHis()"
    >
      <a-timeline mode="left" style="margin-left: 100px;">
        <template v-if="checkList == null || checkList.length == 0">
          <div style="text-align: center; margin-left: -100px; color: #BEBEBE;">
            <img width="120" src="@/assets/images/no.png" /><p>暂无历史</p>
          </div>
        </template>
        <template v-for="item in checkList">
          <a-timeline-item :color="colorFormat(item.status)" :key="item.checkId">
            <!-- 审核人：{{item.createBy}}<br/>
            审核时间：{{item.createTime}} -->
            <p v-if="item.status=='yfb'">提交人：{{item.createBy}}</p>
            <p v-if="item.status=='yfb'">提交时间：{{item.createTime}}</p>
            <p v-if="item.status=='ygb'">下架人：{{item.createBy}}</p>
            <p v-if="item.status=='ygb'">下架时间：{{item.createTime}}</p>
            <p v-if="(item.status=='ybh'||item.status=='ytg'||item.status=='ysh')">审核人：{{item.createBy}}</p>
            <p v-if="(item.status=='ybh'||item.status=='ytg'||item.status=='ysh')">审核时间：{{item.createTime}}</p>
            <p v-if="(item.status=='ybh'||item.status=='ytg'||item.status=='ysh')">审核结果：{{item.status==='ytg'?'已通过':item.status==='ysh'?'已通过':'已驳回'}}</p>
            <p v-if="(item.checkInfo != null && item.checkInfo != '')&&item.status=='ybh'">驳回原因：{{item.checkInfo}}</p>
            <p v-if="(item.checkInfo != null && item.checkInfo != '')&&(item.status=='ytg'||item.status=='ysh')">审核意见：{{item.checkInfo}}</p>
          </a-timeline-item>
        </template>
      </a-timeline>
    </a-modal>
</template>
<script>
import { listCheckCourse } from '@/api/check/course'
import { listCheckStudy } from '@/api/check/study'
import { getListLiveCheck } from '@/api/check/live'
export default {
    data() {
        return {
          dialogVisibleHis:false,
          loading:false,
	        checkList: [],
        }
    },
    methods: {
        getCheckListCourse(courseId){
            this.dialogVisibleHis = true
            var queryParams = {
                courseId: courseId
            }
            listCheckCourse(queryParams).then(response => {
                this.checkList = response.data;
            });
        },
        getCheckListStudy(studyId){
            this.dialogVisibleHis = true
            var queryParams = {
                studyId: studyId
            }
            listCheckStudy(queryParams).then(response => {
                this.checkList = response.data;
            });
        },
        checkListLive(liveId){
            this.dialogVisibleHis = true
            var queryParams = {
                liveId: liveId
            }
            getListLiveCheck(queryParams).then(response => {
                this.checkList = response.data;
            });
        },
        colseDialogHis(){
          this.dialogVisibleHis = false
          this.checkList = []
        },
        colorFormat(status){
          let color="red";
          switch(status) {
              case "ytg":
                  color="green";
                  break;
              case 'ysh':
                  color="green";
                  break;
              case 'yfb':
                  color="yellow";
                  break;
          } 
          return color;
        }
    }
}
</script>