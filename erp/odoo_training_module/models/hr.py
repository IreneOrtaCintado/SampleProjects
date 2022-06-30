# -*- coding: utf-8 -*-
# Part of Odoo. See LICENSE file for full copyright and licensing details.

from odoo import models, fields


class Employee(models.Model):
    _inherit = 'hr.employee'

    seminar_id = fields.Many2many('training_ioc.seminar', string='Seminar')
