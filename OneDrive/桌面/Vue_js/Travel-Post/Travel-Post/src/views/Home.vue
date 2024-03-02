<template>
<div class="main-container">
  <el-container class="container-page1">
    <el-header class="header">
      <Navbar :activeIndex="1"></Navbar>
    </el-header>
    <el-main class="FirstPageClass1">      
      <FirstPage1 class="FirstPage1"></FirstPage1>
    </el-main>
  </el-container>

  <el-container class="container-page2">
    <el-main style="padding: 0;">      
      <FirstPage2></FirstPage2>
    </el-main>
  </el-container>

  <el-container class="container-page3">
    <el-main style="padding: 0;">      
      <FirstPage3></FirstPage3>
    </el-main>
  </el-container>

  <el-container class="container-page4">
    <el-main class="page4">      
      <FirstPage4></FirstPage4>
    </el-main>
  </el-container>

  <el-container class="container-page5">
    <el-footer>
      <Footer></Footer>
    </el-footer>
  </el-container>

</div>
</template>
<script>
import Navbar from "/src/components/HomePage/Navbar.vue";
import FirstPage1 from "/src/components/HomePage/FirstPage1.vue";
import FirstPage2 from "/src/components/HomePage/FirstPage2.vue";
import FirstPage3 from "/src/components/HomePage/FirstPage3.vue";
import FirstPage4 from "/src/components/HomePage/FirstPage4.vue";
import Footer from "/src/components/HomePage/Footer.vue";
export default {
  name: "Home",
  components: {
    Navbar,
    FirstPage1,
    FirstPage2,
    FirstPage3,
    FirstPage4,
    Footer,
  },
};
</script>

<style scoped>
.main-container{
  min-width: 100vw;
  min-height: 100vh;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
}
.header {
  padding: 0px !important;
  margin: 0px !important;
}
.container-page1 {
  min-width: 100%;
  min-height: 100vh;
  padding: 0px !important;
  margin: 0px !important;
  background-image: url('../src/images/neom-seX13AzLqls-unsplash.jpg');
  background-repeat: no-repeat;
  background-size: cover;
}

.container-page2{
  min-width: 100%;
  min-height: 100vh;
  padding: 0px !important;
  margin: 0px !important;
  background-color: black;
  opacity: 0.9;
}
.container-page3{
  min-width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0px !important;
  margin: 0px !important;
  background-color: black;
}

.container-page4{
  min-width: 100%;
  min-height: 100vh;
  flex-direction: column;
  justify-content: center;
  padding: 0px !important;
  margin: 0px !important;
  background-color: black;
  opacity: 0.8;
}

.container-page4 .page4{
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.container-page5{
  min-width: 100%;
  height: auto;
  padding: 0px !important;
  margin: 0px !important;
  background-image: url('../src/images/alexander-andrews-1QK0GQx-jKg-unsplash.jpg');
  background-repeat: no-repeat;
  background-size: cover;
}

.FirstPageClass1{
  width: 100%;
  height: 100%;
  height: inherit;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-content: center;
}

.FirstPage1{
  width: 100%;
  height: 85vh;
  margin: 0 auto;
  display: flex;
  justify-content: center;
}
.el-footer{
  min-width: 100%;
  height: auto;
  padding: 0px !important;
  margin: 0px !important;
  overflow: hidden;
}

</style>
