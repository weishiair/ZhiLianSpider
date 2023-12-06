<template>
  <transition name="fade">
    <div class="company-detail-container">
      <div class="company-detail">
        <h1>{{ companyDetail.companyName }}</h1>
        <p><strong>公司ID：</strong>{{ companyDetail.id }}</p>
        <p><strong>公司规模：</strong>{{ companyDetail.companySize }}</p>
        <p><strong>公司性质：</strong>{{ companyDetail.companyNature }}</p>
        <p><strong>成立时间：</strong>{{ formatDate(companyDetail.establishmentDate) }}</p>
        <p><strong>注册资本：</strong>{{ companyDetail.registeredCapital }}</p>
        <p><strong>公司地址：</strong>{{ companyDetail.companyAddress }}</p>
        <p><strong>公司主页：</strong><a :href="companyDetail.companyWebsite" target="_blank">{{ companyDetail.companyWebsite }}</a></p>
        <p><strong>公司介绍：</strong>{{ companyDetail.companyIntroduce }}</p>
      </div>
    </div>
  </transition>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  name: 'CompanyDetailView',
  props: {
    id: {
      type: Number,
      required: true
    }
  },
  setup(props) {
    const companyDetail = ref({});

    const fetchCompanyDetail = async () => {
      try {
        const response = await axios.get(`companyinfo/get/${props.id}`);
        if (response.data && response.status === 200) {
          companyDetail.value = response.data;
        } else {
          console.error('未找到相应公司信息');
        }
      } catch (error) {
        console.error('获取公司详细信息失败:', error);
      }
    };


    const formatDate = (dateString) => {
      const date = new Date(dateString);
      return date.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' });
    };

    onMounted(fetchCompanyDetail);

    return {
      companyDetail,
      formatDate
    };
  },
};
</script>


<style scoped>
.company-detail-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
  border-radius: 10px;
  background-color: white;
}

.company-detail {
  /* 移除卡片样式，使用简单的居左布局 */
  color: black;
  text-align: left;
}
.company-detail h1 {
  color: black;
  text-align: center;
  margin-bottom: 24px;
}

.company-detail p {
  line-height: 1.8;
  color: black;
  font-size: 16px;
  margin-bottom: 12px;
}

.company-detail p strong {
  font-weight: bold;
}

.company-detail a {
  color: #007BFF;
  text-decoration: none;
}

</style>