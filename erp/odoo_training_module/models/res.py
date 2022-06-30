# -*- coding: utf-8 -*-
# Part of Odoo. See LICENSE file for full copyright and licensing details.


from odoo import models, fields, api


class Partner(models.Model):
    _inherit = 'res.partner'

    is_trainer = fields.Boolean(string='Is Trainer')
    expertise = fields.Many2one('training_ioc.expertise', string='Expertise')
    seminar_id = fields.One2many('training_ioc.seminar', 'trainer_id', string='Seminar')
