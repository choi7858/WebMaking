<template>
    <div class="border p-6 rounded-lg shadow-md">
        <h2 class="text-xl font-bold mb-4">{{ title }}</h2>
        <input v-model="contentValue" type="text" class="border p-2 w-full" />
    </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, watch } from "vue";

const props = defineProps({
    title: String,
    content: String,
});

const emits = defineEmits(["update"]);
const contentValue = ref(props.content);

watch(contentValue, (newVal) => {
    emits("update", newVal);
});
</script>