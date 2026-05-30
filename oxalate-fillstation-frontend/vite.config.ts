import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'
import {fileURLToPath} from 'node:url'
import {resolve} from 'node:path'

const projectRoot = fileURLToPath(new URL('.', import.meta.url))

// https://vite.dev/config/
export default defineConfig({
    resolve: {
        alias: [
            {
                find: /^@rc-component\/util$/,
                replacement: resolve(projectRoot, 'src/shims/rcComponentUtil.ts'),
            },
        ],
    },
    plugins: [
        react(),
    ],
})
