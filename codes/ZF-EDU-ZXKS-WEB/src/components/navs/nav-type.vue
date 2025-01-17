<template>
  <div class="nav-type-container" :class="showMore? 'showheight' : '' " >
    <p class="nav-title">{{ nav }}：</p>
    <p class="all-type" :class="{ active: checkNav == '' }" @click="changeNav('', type)">全部</p>
    <div class="nav-list">
      <p
        class="nav-txt"
        :class="{ active: checkNav == item }"
        @click="changeNav(item, type)"
        v-for="(item, index) in list"
        :key="index"
      >
        {{ type == 'xq' ? item.title : item.dictLabel }}
      </p>
    </div>
    <div class="show-btn" @click="showFun" v-if="isShowHeight">
      <span class="show-txt">{{ !showMore ? '收起' : '更多' }}</span>
      <i class="iconfont icon-xiala" :class="{ show: !showMore }"></i>
    </div>
  </div>
</template>

<script>
export default {
  props: ['nav', 'list', 'type'],
  data() {
    return {
      isShowHeight: false,
      showMore: false,
      checkNav: '',
      navHeight: '',
    };
  },
  created() {
    this.$nextTick(() => {
      // console.log(document.querySelector('.nav-list').heght)
      if(this.type == 'zy' && this.list.length < 12){
        this.isShowHeight = false;
      } else if(this.type == 'lx' && this.list.length < 7) {
        this.isShowHeight = false;
      }else {
        this.isShowHeight = document.querySelector('.nav-list').offsetHeight >= 36;
      } 
      
    });
  },
  methods: {
    showFun() {
      this.navHeight = document.querySelector('.nav-list').offsetHeight + 'px';
      this.showMore = !this.showMore;
    },
    changeNav(item, type) {
      this.checkNav = item ? item : '';
      this.$emit('handleNav', item, type);
    },
  },
};
</script>

<style lang="scss" scoped>
.showheight{
  height: 36px;
}
.nav-type-container {
  display: flex;
  align-items: flex-start;
  width: 100%;
  // height: 36px;
  margin: 0 0 18px;
  border-bottom: 1px dashed #ccc;
  transition: all 150ms linear;
  overflow: hidden;
  .nav-title {
    flex-shrink: 0;
    width: 90px;
    @include txts(22, #333);
    text-align: right;
  }
  .all-type {
    flex-shrink: 0;
    padding: 2px;
    @include txts(22, #333);
    cursor: pointer;
    &.active {
      background: $themeColor;
      color: #fff;
    }
  }
  .nav-list {
    flex: 1;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    width: 100%;
    padding: 0 30px;
    .nav-txt {
      height: 35px;
      padding: 2px 8px 0;
      margin: 0 20px 10px 0;
      @include txts(22, #333);
      cursor: pointer;
      &.active {
        background: $themeColor;
        color: #fff;
      }
    }
  }
  .show-btn {
    flex-shrink: 0;
    display: flex;
    align-items: center;
    height: 26px;
    @include txts(22, #333);
    cursor: pointer;
    .iconfont {
      @include txts(18, #333);
      transition: all 150ms linear;
      &.show {
        transform: rotate(180deg);
      }
    }
  }
}
</style>
