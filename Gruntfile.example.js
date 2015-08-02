module.exports = function(grunt) {
    grunt.loadNpmTasks('grunt-screeps');

    grunt.initConfig({
        screeps: {
            options: {
                email: '<your e-mail>',
                password: '<your password>',
                branch: 'default'
            },
            dist: {
                src: ['target/scrAI-deploy/main.js']
            }
        }
    });
}