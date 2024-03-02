<template>
    <el-row class="el-row">
        <el-col :span="8" class="feature">
            <h1>Big Journey Start
                <br>
                Find the favorite spots you like
                <br>
                Score all the landscapes you travelled 
                <br>Post the treasured moment</h1>
            <h1>Share your exprience</h1>
            <el-button type="primary" size="large" dark="true" round>
                Find the spots
            </el-button>
        </el-col>
        <el-col :span="16" class="right-page2">
            <el-carousel
                class="el-carousel-vertical-page2"
                direction="vertical"
                type="card"
                :autoplay="ture"
            >
                <el-carousel-item v-for="img in imgs" :key="img.id" class="el-carousel-item">
                    <el-image :src="imgs[img.id-1].url" :fit="fit" class="el-image"/>
                </el-carousel-item>
            </el-carousel>
        </el-col>
    </el-row>
</template>
<script>
export default {
    name: "FirstPage2",
    data() {
        return {
            imgs: [
                {
                    id: 1,
                    url: "../src/images/meckl-antal-XxjFXaMf1-4-unsplash.jpg",
                },
                {
                    id: 2,
                    url: "../src/images/josh-withers-o1kRju7UvR0-unsplash.jpg",
                },
                {
                    id: 3,
                    url: "../src/images/marek-piwnicki-294rbxuV_SA-unsplash.jpg",
                },
                {
                    id: 4,
                    url: "../src/images/neom-gOqBe7ropxM-unsplash.jpg",
                },
                {
                    id: 5,
                    url: "../src/images/leo_visions-iSM8Z82uxn8-unsplash.jpg",
                },
                {
                    id: 6,
                    url: "../src/images/krisztina-papp-y2qPCzQvUU4-unsplash.jpg",
                },
                {
                    id: 7,
                    url: "../src/images/colin-watts-V8TrkOMCDgQ-unsplash.jpg",
                },
                {
                    id: 8,
                    url: "../src/images/aneesh-matcha-2q2CzySZlUI-unsplash.jpg",
                },         
            ],
        };
    },
    methods: {
    },
};
</script>
<style>
.el-row{
    padding: 0;
    margin: 0;
    width: 100%;
    height: 100%;
}
.feature{
    display: flex;
    background-color: black;
    opacity: 0.9;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin: 0 auto;
    color: white;
    text-align: center;
}
.feature h1{
    font-size: 2em;
}
.feature h2{
    font-size: 1.5em;
}
.el-button{
    margin-top: 5%;
    font-size: large;
    text-align: center;
}
.right-page2{
   display: flex;
    justify-content: center;
    align-items: center;
}
.el-carousel-vertical-page2{
    background-color: black;
    width: 100%;
    height: 100%;
    padding: 0 5% 0 5%;
    overflow: hidden;
}
.el-carousel-item{
    margin: 0 5% 0 5%;
    width: 90%;
    height: auto;
    overflow: hidden;
}
.el-image{
    width: 100%;
    height: 100%;
    overflow: hidden;
}
</style>