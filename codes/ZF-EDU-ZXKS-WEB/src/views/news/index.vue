<template>
  <div class="page-container">
    <!-- <banner :imgList="$store.getters.getBanner" v-if="$store.getters.getBanner.length"></banner> -->
    <div class="news-container">
      <div class="news-left-list">
        <div class="news-left-title">
          <img src="../../assets/img/news/news_icon.png" alt="" class="news-icon" />
          <p>资讯栏</p>
        </div>
        <div class="news-type-list">
          <p
            class="news-type" 
            :class="{ active: checkPart.channelId == item.channelId }"
            v-for="(item, index) in partList"
            :key="index"
            @click="handlePart(item)"
          >
            <i class="iconfont icon-gjiantous"></i>
            {{ item.channelName }}
          </p>
        </div>
      </div>
      <div class="news-right-list" v-loading="loading">
        <div class="news-right-title">
          <p class="check-news-title">{{ checkPart.channelName }}</p>
          <div class="news-crumb">
            <p>当前位置：</p>
            资讯栏 > {{ checkPart.channelName }}
          </div>
        </div>
        <router-link
          :to="`/news-info/${item.contentId}/${checkPart.channelId}`"
          class="news-list-box"
          v-for="(item, index) in newsList"
          :key="index"
        >
          <el-image v-if="item.contentImg" :src="item.contentImg && item.contentImg.split(',')[0]" fit="cover">
            <template #error>
              <el-image fit="cover" :src="require('../../assets/img/news/news_1.png')"></el-image>
            </template>
          </el-image>
          <div class="news-intro-box">
            <p class="news-title">{{ item.title }}</p>
            <p class="news-time">
              <i class="iconfont icon-time"></i>
              {{ item.createTime.slice(0, 10) }}
            </p>
            <div class="news-info">
              {{ item.contentDescribe.length > 56 ? item.contentDescribe.slice(0, 56) : item.contentDescribe }}
              <span v-if="item.contentDescribe.length > 56">......</span>
              <div class="show-more" >[详细]</div>
            </div>
          </div>
        </router-link>

        <empty-page
          v-if="isEmpty"
          :defaultImg="require('../../assets/img/default/empty_img.png')"
          :defaultTip="'暂无数据...'"
        ></empty-page>
      </div>
    </div>
    <div class="pagination-box" v-if="newsAllNum > pageSize">
      <el-pagination
        background
        :page-size="pageSize"
        :current-page="currentPage"
        layout="prev, pager, next"
        :prev-text="'上一页'"
        :next-text="'下一页'"
        :total="newsAllNum"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import Banner from '@/components/banner/banner.vue';
import EmptyPage from '@/components/default/empty-page.vue';
import Cookies from 'js-cookie';
import { getNewsPart, getNewsList } from '@/api/home';
export default {
  name: 'News',
  data() {
    return {
      bannerList: [],
      partList: [],
      checkPart: {},
      loading: false,
      isEmpty: false,
      pageSize: 10,
      currentPage: 1,
      newsAllNum: 0,
      newsList: [],
      siteId: Cookies.get('checkCitySiteId')
    };
  },
  created() {
    this.getNewsPart();
  },
  methods: {
    getNewsPart() {
      getNewsPart({ parentId: 0 }).then(({ data }) => {
        this.partList = data;
        
        if(this.$route.query.channelId){
          this.checkPart.channelId = this.$route.query.channelId;
          this.checkPart.channelName = this.$route.query.channelName;
        }else{
          this.checkPart = Object.assign({}, this.checkPart, data[0]);
        }
        
        // let arr = [];
        // data.forEach(ele => {
        //   if (ele.channelImg) arr.push(ele.channelImg);
        // });
        // this.$store.commit('updateNewsBanner', arr);
        this.getNewsList();
      });
    },
    getNewsList() {
      getNewsList({
        // siteId: Cookies.get('checkCitySiteId'),
        channelId: this.checkPart.channelId,
        pageSize: this.pageSize,
        pageNum: this.currentPage,
      }).then(({ data }) => {
        this.loading = false;
        this.newsAllNum = data.total;
        if (!data.total) {
          this.isEmpty = true;
          return;
        }
        this.newsList = data.rows;
      });
    },
    handlePart(item) {
      this.loading = true;
      this.isEmpty = false;
      this.checkPart = Object.assign({}, this.checkPart, item);
      this.currentPage = 1;
      this.newsList = [];
      this.getNewsList();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getNewsList();
    },
  },
  components: {
    Banner,
    EmptyPage,
  },
};
</script>

<style lang="scss">
.news-container {
  display: flex;
  justify-content: space-between;
  width: 1200px;
  min-height: 600px;
  padding: 50px 0;
  margin: auto;
  .news-left-list {
    flex-shrink: 0;
    width: 240px;
    .news-left-title {
      display: flex;
      align-items: center;
      height: 46px;
      border-bottom: 1px solid $themeColor;
      @include txts(24, #333, 600);
      .news-icon {
        flex-shrink: 0;
        width: 34px;
        height: auto;
        margin: 0 20px 0 0;
      }
    }
    .news-type-list {
      width: 100%;
      padding: 0 0 0 24px;
      .news-type {
        display: flex;
        align-items: center;
        margin: 40px 0 0;
        @include txts(24, #333);
        cursor: pointer;
        .iconfont {
          margin: 0 16px 0 0;
          font-size: 20px;
        }
        &.active {
          color: $themeColor;
        }
      }
    }
  }
  .news-right-list {
    flex: 1;
    width: 100%;
    padding: 0 0 0 40px;
    .news-right-title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      height: 46px;
      border-bottom: 1px solid #ccc;
      .check-news-title {
        @include txts(26, #333, 600);
      }
      .news-crumb {
        display: flex;
        align-items: center;
        @include txts(22, #7b7b7b);
      }
    }
  }
  .news-list-box {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    margin: 40px 0 0;
    .el-image {
      flex-shrink: 0;
      width: 260px;
      height: 150px;
      border-radius: 8px;
      overflow: hidden;
    }
    .news-intro-box {
      flex: 1;
      width: 100%;
      padding: 0 0 0 24px;
      .news-title {
        @include txts(24, #333, 600);
      }
      .news-time {
        display: flex;
        align-items: center;
        padding: 10px 0 16px;
        @include txts(24, #9c9c9c);
        .iconfont {
          margin: 0 10px 0 0;
        }
      }
      .news-info {
        @include txts(22, #333);
        line-height: 34px;
        .show-more {
          display: inline;
          color: $themeColor;
        }
      }
    }
  }
}
</style>
