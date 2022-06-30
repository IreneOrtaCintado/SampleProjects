# -*- coding: utf-8 -*-
{
    'name': "training_ioc",

    'summary': """
        Training for Employees""",

    'description': """
        Collection of courses offered to employees. It gives access to information about the courses, the trainers and the courses attended by the employees.
    """,

    'author': "Toys Gathered",
    'website': "http://www.toysgathered.com",

    # Categories can be used to filter modules in modules listing
    # Check https://github.com/odoo/odoo/blob/13.0/odoo/addons/base/data/ir_module_category_data.xml
    # for the full list
    'category': 'Generic Modules',
    'version': '0.1',

    # any module necessary for this one to work correctly
    'depends': ['base','hr'],

    # always loaded
    'data': [
        # Security
        'security/security.xml',
        'security/ir.model.access.csv',
        # Views
        'views/views.xml',
        'views/templates.xml',
        'views/hr.xml',
        'views/res.xml',
        # Demo data
        #'demo/hr_demo.xml',
        #'demo/res_partner_demo.xml',
        #'demo/demo.xml',

    ],
    # only loaded in demonstration mode
    'demo': [
        'demo/hr_demo.xml',
        'demo/res_partner_demo.xml',
        'demo/demo.xml',
    ],
    'installable': True,
    'application': True,
}
