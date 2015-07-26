module.exports = function(grunt) {
    grunt.loadNpmTasks('grunt-screeps');

    grunt.initConfig({
        screeps: {
            options: {
                email: '<your e-mail>',
                password: '<your password>'
            },
            dist: {
                src: ['target/scrAI-deploy/**/scrAI/**/*.js', 'src/main/javascript/*.js']
            }
        }
    });
}