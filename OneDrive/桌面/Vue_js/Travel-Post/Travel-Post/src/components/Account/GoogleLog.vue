<template>
    <button @click="SignInButton" class="LogButton"><i class="bi bi-google LogIcon"></i></button>
</template>
<script>
export default {
  name: 'GoogleLogin',
  data() {
    return {
      user: null,
      gapi: null,
      gapiLoaded: false, // 新增 gapiLoaded 标志
    };
  },

  mounted() {
    this.loadGapi();
  },

methods: {
  loadGapi() {
      if (window.gapi) {
        this.gapiLoaded = true;
        this.initializeGoogleAuth();
      } else {
        // 如果 gapi 未加载，等待一段时间后重试
        setTimeout(() => {
          this.loadGapi();
        }, 100);
      }
    },

    async initializeGoogleAuth() {
      try {
        await new Promise((resolve) => {
          window.gapi.load('auth2', resolve);
        });
        
        this.gapi = window.gapi;

        await this.gapi.auth2.init({
          client_id: '39085943694-3ghgq85tea94vgtt84bgbm17077smn6g.apps.googleusercontent.com',
        });

      } catch (error) {
        console.error(error);
      }
    },

    SignInButton(){
    this.gapi.auth2.getAuthInstance().signIn().then((googleUser) => {
    // 登录成功后的操作
    const profile = googleUser.getBasicProfile();
    // 处理用户信息
    this.user = {
      id: profile.getId(),
      name: profile.getName(),
      email: profile.getEmail(),
      imageUrl: profile.getImageUrl(),
    };
    this.$emit('GetUser', this.user);
  });
  },


  handleLogout() {
    const auth2 = this.gapi.auth2.getAuthInstance();
    auth2.signOut().then(() => {
      this.user = null;
    });
  },
}

};
</script>
<style scoped>
.LogButton{
   width: 100% !important;
   height: 100% !important;
   padding: 0 !important;
   margin: 0 !important;
   border: none !important;
   background-color: inherit !important;
   z-index: 1000;
}
.LogIcon{
   width: 100% !important;
   height: 100% !important;
   color: white !important;
}
</style>