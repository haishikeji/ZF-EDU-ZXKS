<template>
  <div class="user-center-manage-wrapper user-gray-border">
    <div class="learning-wrapper">
      <div>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane :label="'我学的班(' + learningSum + ')'" name="starting"></el-tab-pane>
        </el-tabs>
      </div>
      <!-- <div class="curriculum curriculum-list">
        <el-tabs v-model="activeName1" @tab-click="handleClick1">
          <el-tab-pane label="全部" name="all_" typeId="0">
          </el-tab-pane>
          <el-tab-pane :label="item.title" :name="'all_'+item.id" v-for="(item, index) in paneList" :key="index">
          </el-tab-pane>
        </el-tabs>
      </div> -->
  </div>
  <div class="flexbox">
    <div v-for="(item, index) in culumList" :key="index" style="width:100%;margin-bottom:10px;">
      <myclass-list
        :classlist="item"
        @delHandle="delHandle"
      ></myclass-list>
    </div>

  </div>
    <div class="pagination-box" v-if="pageAllNum > pageSize">
      <el-pagination
        background
        :page-size="pageSize"
        layout="prev, pager, next"
        :current-page="currentPage"
        :prev-text="'上一页'"
        :next-text="'下一页'"
        :total="pageAllNum"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
    <empty-page
      v-if="!pageAllNum && !culumList.length"
      :defaultImg="require('../../assets/img/default/empty_img.png')"
      :defaultTip="'暂无学习记录...'"
    ></empty-page>
  </div>
</template>

<script>
import myclassList from "@/components/myclass/myclass.vue";
import EmptyPage from "@/components/default/empty-page.vue";
import { getCourseUseList, getCourseCount, delStudyById } from "@/api/user";
export default {
  name: "UserCulum",
  data() {
    return {
      paneList: [],
      activeName: "starting",
      activeName1: "all_",
      learningSum: 0,
      endSum: 0,
      pageAllNum: 0,
      pageSize: 10,
      currentPage: 1,
      culumList: [],
      typeId: "",
    };
  },
  created() {
    this.getCulum();
    this.getCourseCount();
  },
  methods: {
    delHandle(id){
      delStudyById(id).then(res => {
        this.$message({
          message: res.msg,
          type: 'success',
        });
        this.getCulum();
        this.getCourseCount()
      })
    },
    getCourseCount() {
      this.getCourseUseList()
      // getCourseCount().then(data => {
      //   this.endSum = data.data.endSum;
      //   this.paneList = data.data.aAppObjTypeList;
      //   this.getCourseUseList()
      // })
    },
    getCourseUseList() {
      getCourseUseList({
        typeId: this.typeId,
        pageSize: this.pageSize,
        pageNum: this.currentPage,
      }).then(data => {
        this.culumList = data.rows;
        this.pageAllNum = data.total;
      })
    },
    getCulum() {
      getCourseUseList({
        //   this.$store.getters.getUser ? this.$store.getters.getUser.userId : ''
        // 封面：coverImg
        // 名字 ：title
        // 绘画  objTypes【0】.title
        // studyNum 多少人在学
        // userId: 144,
        // userId: this.$store.getters.getUser ? this.$store.getters.getUser.userId : '',
        pageSize: this.pageSize,
        pageNum: this.currentPage,
      }).then((data) => {
        this.culumList = data.rows;
        this.pageAllNum = data.total;
        this.learningSum = data.total;
      });
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getCulum();
    },
    handleClick1(tab, event) {
      this.culumList = [];
      if (tab.name === 'all_') {
        this.typeId = '';
      } else {
        this.typeId = tab.name.split('_')[1];
      }
      this.getCourseUseList();
    },
    handleClick(tab, event) {
      this.activeName = tab.name;
    },
  },
  components: {
    myclassList,
    EmptyPage,
  },
};
</script>

<style lang="scss" scoped>
.learning-wrapper {
  position: relative;
  .curriculum-list {
    width: 500px;
    position: absolute;
    right: 0;
    top: 0;
  }
}
.flexbox {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
}
</style>
