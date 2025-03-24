const { body, validationResult } = require('express-validator');
const constants = require('./constants');
const util = require('util');

const config = {
    password_config: {
        minLength: 8,
        minNumbers: 1,
        minSymbols: 1,
        minUppercase: 1,
        minLowercase: 1
    }
};

module.exports = {
    validators: [
        body('email').isEmail()
            .withMessage(constants.EMAIL_ERROR),
        body('password').isStrongPassword(config.password_config)
            .withMessage(util.format(constants.PASSWORD_ERROR,
                config.password_config.minLength,
                config.password_config.minSymbols,
                config.password_config.minUppercase,
                config.password_config.minLowercase,
                config.password_config.minNumbers,
            )),
        body('username').isAlphanumeric().withMessage('username chỉ được chứa chữ và số'),
        body('role').isIn(constants.USER_PERMISSION).withMessage('role không hợp lệ'),

        // ✅ THÊM KIỂM TRA fullname chỉ chứa chữ
        body('fullname')
            .matches(/^[A-Za-zÀ-Ỹà-ỹ\s]+$/)
            .withMessage('fullname chỉ được chứa chữ cái và khoảng trắng'),

        // ✅ THÊM KIỂM TRA imgURL phải là URL hợp lệ
        body('imgURL')
            .isURL()
            .withMessage('imgURL phải là URL hợp lệ')
    ],
    
    validator_middleware: function (req, res, next) {
        let errors = validationResult(req);
        if (errors.isEmpty()) {
            next();
        } else {
            res.status(400).send(errors.array());
        }
    }
};
