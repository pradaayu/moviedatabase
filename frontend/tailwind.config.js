/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'primary': '#84cc16',
        'dark': '#161616',
        'dark-light': '#222222',
      },
    },
  },
  plugins: [],
}