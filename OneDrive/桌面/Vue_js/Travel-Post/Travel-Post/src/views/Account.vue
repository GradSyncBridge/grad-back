<template>
  <el-container>
    <el-header>
      <Navbar :activeIndex="5"></Navbar>
    </el-header>
    <el-main :style="ActiveClass()">
        <LogIn v-if="User == null" @GetUser="GetUser"></LogIn>
        <AcountManagement v-else></AcountManagement> 
    </el-main>
  </el-container>
</template>

<script>
import Navbar from "/src/components/HomePage/Navbar.vue";
import LogIn from '/src/components/Account/login.vue';
import AcountManagement from '/src/components/Account/AcountManagement.vue';
export default{
  name: 'Account',
  components: {
    LogIn,
    Navbar,
    AcountManagement,
},
  data() {
    return {
      User: null,
    };
  },
  methods: {
    GetUser(UserData) {
      this.User = UserData;
    },
    ActiveClass() {
      if (this.User != null) {
            console.log('User is not null');
            return {'background-color': 'white'};
      }
    },
  },
}
</script>

<style scoped>

.el-header{ 
    margin: 0 !important;
    padding: 0 !important;
}
.el-container{
    min-width: 100vw;
    min-height: 100vh;
    margin: 0 !important;
    padding: 0 !important;
    overflow-x: hidden;
    background-image: url('../src/images/neom-seX13AzLqls-unsplash.jpg');
    background-repeat: no-repeat;
    background-size: cover;
    opacity: 0.9;
}
.el-main{
    display: flex;
    justify-content: center;
    align-items: center;
    min-width: 100vw;
    max-height: 900vh;
    overflow-x: hidden;
    margin: 0 !important;
    padding: 0 !important;
}
</style>

