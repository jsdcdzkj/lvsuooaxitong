<template>
    <div class="middle-body">
        <div class="middle-header">
            <div :class="['middle-title', $slots.title ? 'none' : '']">
                <template v-if="$slots.title">
                    <slot name="title"></slot>
                </template>
                <template v-else>
                    {{ title }}
                </template>
            </div>
            <div class="middle-right" v-if="showRight">
                <template v-if="$slots.right">
                    <slot name="right"></slot>
                </template>
                <template v-else>
                    <el-button
                        icon="el-icon-plus"
                        type="primary"
                        size="small"
                        @click="handleAdd()"
                        >{{ buttonText }}</el-button
                    >
                </template>
            </div>
        </div>
        <div :style="{ height: height, padding: '15px', overflow: 'hidden', overflowY: 'auto' }">
            <slot></slot>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        height: {
            type: String,
            default: 'auto',
        },
        title: {
            type: String,
            default: '标题',
        },
        buttonText: {
            type: String,
            default: '按钮',
        },
        showRight: {
            type: Boolean,
            required: true,
        },
    },
    data() {
        return {};
    },
    methods: {
        handleAdd() {
            this.$emit('handleAdd');
        },
    },
};
</script>

<style lang="scss" scoped>
.middle-body {
    border: 1px solid #ebeef5;
    background-color: #fff;
    overflow: hidden;
    overflow-y: auto;
    position: relative;
    border-radius: 8px;
}

.middle-header {
    width: 100%;
    height: 48px;
    // position: absolute;
    // top: 0;
    background-color: #fff;
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid #ebeef5;
}

.middle-title {
    height: 48px;
    line-height: 48px;
    font-weight: bold;
    text-indent: 35px;
    position: relative;
}

.middle-title::before {
    content: '';
    width: 5px;
    height: 16px;
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    left: 18px;
    background-color: #1a8cfe;
    border-radius: 3px;
}
.middle-title.none {
    text-indent: 20px;
}
.middle-title.none::before {
    display: none;
}

.middle-right {
    padding-right: 20px;
    height: 48px;
    line-height: 48px;
}
</style>
