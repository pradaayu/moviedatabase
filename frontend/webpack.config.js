const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  // Change entry point to TypeScript file
  entry: './src/index.tsx',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'bundle.js',
  },
  module: {
    rules: [
      {
        // Update test regex to include TypeScript files
        test: /\.(ts|tsx|js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
        },
      },
      {
        test: /\.css$/,
        use: [
          'style-loader', // Injects CSS into the DOM
          'css-loader', // Interprets @import and url()
          'postcss-loader' // Processes CSS with PostCSS (including Tailwind)
        ]
      },
    ],
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './public/index.html',
    }),
    // Remove the PostCSS plugins from here
  ],
  resolve: {
    // Add TypeScript extensions to resolution
    extensions: ['.tsx', '.ts', '.js', '.jsx'],
  },
  devServer: {
    static: {
      directory: path.join(__dirname, 'public'),
    },
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    }
  },
};