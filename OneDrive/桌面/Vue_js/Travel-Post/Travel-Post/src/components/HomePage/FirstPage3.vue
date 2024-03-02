<template>
 <el-row class="el-row1">
    <el-col :span="8" v-for="Firstphotocard in Firstphotocards" :key="Firstphotocard.id">
        <el-card class="el-card1">
            <el-image :src="Firstphotocards[Firstphotocard.id-1].url" :fit="fit" class="el-image1"/>
        <div style="margin-bottom: 5%;">
          <h5>{{ Firstphotocards[Firstphotocard.id-1].title }}</h5>
          <h4>{{ Firstphotocards[Firstphotocard.id-1].subtitle }}</h4>
        </div>
      </el-card>
    </el-col>
 </el-row>
 <el-row class="el-row2">
    <el-col :span="12" v-for="Secondphotocard in Secondphotocards" :key="Secondphotocard.id">
        <el-card class="el-card2">
            <el-image :src="Secondphotocards[Secondphotocard.id-1].url" :fit="fit" class="el-image2"/>
        <div style="margin-bottom: 5%;">
            <h5>{{ Secondphotocards[Secondphotocard.id-1].title }}</h5>
            <h4>{{ Secondphotocards[Secondphotocard.id-1].subtitle }}</h4>
        </div>
      </el-card>
    </el-col>
 </el-row>
</template>
<script>
export default{
    name: "FirstPage3",
    data()
    {
        return {
            Firstphotocards:[
                {
                   id: 1,
                   title: "GET REWARDED WITH ONE KEY",
                   subtitle: "Earn OneKeyCash on eligible flights, hotels, cars, and more",
                   url: "../src/images/pascal-debrunner--Noa043YDX0-unsplash.jpg",
                },
                {
                   id: 2,
                   title: "INTRODUCING PRICE DROP PROTECTION",
                   subtitle: "Get our best value on your flight with Price Drop Protection",
                   url: "../src/images/mason-field-TaHWuzdpcg0-unsplash.jpg",
                },
                {
                    id: 3,
                    title: "SUNNY WINTER GETAWAYS",
                    subtitle: "Escape the cold with sunshine and savings",
                    url: "../src/images/tyler-casey-MtRVfdKWbPI-unsplash.jpg",
                },

            ],
            Secondphotocards:[
                {
                   id: 1,
                   title: "MAKE FOR THE MOUNTAINS",
                   subtitle: "Ready for some much-needed R & R? Let's get back to nature",
                   url: "../src/images/mario-von-rotz-n1SdmFGyxd4-unsplash.jpg",
                },
                {
                   id: 2,
                   title: "SAVE ON FLIGHTS TO GET GOING",
                   subtitle: "Flight deals ready to take you to the most-loved places",
                   url: "../src/images/pascal-debrunner-csQ3Bad5b8M-unsplash.jpg",
                },
            ],
        }
    },
}
</script>
<style scoped>

h1, h2, h3, h4, h5, h6 {
  font-family: inherit; /* Use the default font family */
  font-weight: inherit; /* Use the default font weight */
  margin: 0; /* Reset margin */
  line-height: 1.2; /* Set a consistent line height */
}
.el-row1{
    width: 100%;
    height: 45%;
    padding: 2% 2% 0 2%;
    color: white;
    margin: 0;
    overflow: hidden;
}
.el-card1{
    width: 100%;
    height: 100%;
    background-color: black;
    border: none;
    color: white;
    overflow: hidden;
}
.el-image1{
    width: 100%;
    border-radius: 7%;
    height: auto;
    padding: 0;
    margin: 0;
    overflow: hidden;
}
.el-row2{
    width: 100%;
    height: 55%;
    padding: 0 2% 0 2%;
    margin: 0;
    overflow: hidden;
}

.el-card2{
    width: 100%;
    height: 100%;
    background-color: black;
    border: none;
    color: white;
    padding: 0;
    margin: 0;
    overflow: hidden;
}
.el-image2{
    width: 100%;
    height: auto;
    border-radius: 10%;
    padding: 0;
    margin: 0;
    overflow: hidden;
}
.el-col{
    padding: 0 1% 0 1%;
    margin: 0;
    width: 100%;
    height: 100%;
}

</style>