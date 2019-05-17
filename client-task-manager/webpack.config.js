const path = require('path');
const VueLoaderPlugin = require('vue-loader/lib/plugin');

module.exports = {
    mode: 'development',
    devtool: 'source-map',
    entry: path.join(__dirname, 'js', 'main.js'),
    devServer: {
        contentBase: './dist',
        compress: true,
        port: 7575,
        allowedHosts: [
            '178.140.58.252:6060'
        ]
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: [
                    /(node_modules|bower_components)/,
                     require.resolve('bootstrap-vue')
                    ],
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            },
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.vue$/,
                loader: 'vue-loader'
            }
        ]
    },
    plugins: [
        new VueLoaderPlugin()
    ],
    resolve: {
        modules: [
            path.join(__dirname, 'js'),
            path.join(__dirname, 'node_modules'),
        ],
    }
}