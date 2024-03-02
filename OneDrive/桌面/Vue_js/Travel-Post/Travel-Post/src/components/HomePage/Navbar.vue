<template>
     <el-menu
     fixed
    :default-active="activeIndex"
    ref="menu"
    class="el-menu-demo"
    mode="horizontal"
    text-color="#fff"
    collapse-transition="true"
    background-color="#000000"
    default-active="activeIndex"
  >
    <el-menu-item index="1">
      <router-link to="/" class="LinkCon">
        Home
      </router-link>
    </el-menu-item>
    <el-sub-menu index="2"
    >
      <template #title>Post</template>
      <el-menu-item index="2-1">item one</el-menu-item>
      <el-menu-item index="2-2">item two</el-menu-item>
      <el-menu-item index="2-3">item three</el-menu-item>
      <el-sub-menu index="2-4">
        <template #title>item four</template>
        <el-menu-item index="2-4-1">item one</el-menu-item>
        <el-menu-item index="2-4-2">item two</el-menu-item>
        <el-menu-item index="2-4-3">item three</el-menu-item>
      </el-sub-menu>
    </el-sub-menu>
    <el-menu-item index="3" >Comment</el-menu-item>
    <el-menu-item index="4">
      <router-link to="/about" class="LinkCon">
        About
      </router-link>
    </el-menu-item>
    <el-menu-item index="5">
      <router-link to="/account" class="LinkCon">
        Account
      </router-link>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: 'Navbar',
  data() {
    return {
    }
  },
  props: ['activeIndex'],
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath)
    }
  },
  mounted() {
    this.$nextTick(() => {
    window.addEventListener('scroll', () => {
      const scrollTop = window.scrollY;
      const menuRef = this.$refs.menu;
      
      if (menuRef) {
        const menu = menuRef.$el;

        if (scrollTop > 100) {
          menu.style.top = '0';
          menu.style.position = 'fixed';
          menu.style.backgroundColor = '#000000';
          menu.style.opacity = '1';
          menu.style.zIndex = '100';
        } else {
          menu.style.position = 'relative';
          menu.style.backgroundColor = ''; // 恢复初始背景颜色
          menu.style.opacity = ''; // 恢复初始透明度
          menu.style.zIndex = ''; // 恢复初始层级
        }
      }
    });
  });
  },
}
</script>

<style> 
.el-menu-demo {
  min-width: 100%;
  display: flex; 
  margin: 0 auto;
  justify-content: center;
  align-items: center;
  padding: 0 !important;
  background-color: transparent;
  z-index: 100;
}
.el-menu .el-menu-item{
  margin: 1%;
}
.el-menu .el-sub-menu{
  margin: 1%;
}
.LinkCon{
  text-decoration: none;
}
</style>