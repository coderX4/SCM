/** @type {import('tailwindcss').Config} */
export default {
  content: ["./src/main/resources/**/*.{html,js}"],
  theme: {
    extend: {screens: {
        'custom-lg': { min: '1024px' },
      },},
  },
  plugins: [],
  darkMode:"selector",
}


