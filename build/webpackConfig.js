const path = require('path')
const HtmlWebPackPlugin = require('html-webpack-plugin');
const devMode = process.env.NODE_ENV !== 'production'

function resolve(relatedPath) {
    return path.join(__dirname, relatedPath)
}

module.exports = {
    entry: './src/index.js',
    output: {
        path: resolve('../dist'),
        filename: devMode ? 'js/[name].[hash].js' : 'js/[name].[contenthash].js',
        chunkFilename: devMode ? 'chunks/[name].[hash:4].js' : 'chunks/[name].[contenthash].js'
    },
    devtool: devMode ? "cheap-module-source-map" : undefined,
    devServer: {
        contentBase: [path.join(__dirname, '../dist'), path.join(__dirname, '../src')],
        publicPath: '/',
        host: '127.0.0.1',
        port: 8887,
        headers: {
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "POST,GET,OPTIONS,DELETE",
            "Access-Control-Allow-Headers": "x-requested-with,content-type"
        }
    },
    resolve: {
        extensions: ['.wasm', '.mjs', '.js', '.json', '.jsx']
    },
    plugins: [
        new HtmlWebPackPlugin({
            template: 'public/index.html',
            filename: 'index.html',
            inject: true
        })
    ],
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-react'],
                        cacheDirectory: true
                    }
                }
            }
        ]
    }
};