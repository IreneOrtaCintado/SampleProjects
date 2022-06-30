# -*- coding: utf-8 -*-
# from odoo import http


# class FormacionIoc(http.Controller):
#     @http.route('/training_ioc/training_ioc/', auth='public')
#     def index(self, **kw):
#         return "Hello, world"

#     @http.route('/training_ioc/training_ioc/objects/', auth='public')
#     def list(self, **kw):
#         return http.request.render('training_ioc.listing', {
#             'root': '/training_ioc/training_ioc',
#             'objects': http.request.env['training_ioc.training_ioc'].search([]),
#         })

#     @http.route('/training_ioc/training_ioc/objects/<model("training_ioc.training_ioc"):obj>/', auth='public')
#     def object(self, obj, **kw):
#         return http.request.render('training_ioc.object', {
#             'object': obj
#         })
