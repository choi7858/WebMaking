<template>
    <div class="border p-6 rounded-lg shadow-md hover:shadow-lg cursor-pointer" @click="selectTemplate">
        <h2 class="text-xl font-bold">{{ template.name }}</h2>
        <p class="text-gray-600 mt-2">{{ template.description }}</p>
    </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({
    template: Object,
});

const emits = defineEmits(["select"]);

const selectTemplate = () => {
    emits("select", props.template);
};
</script>