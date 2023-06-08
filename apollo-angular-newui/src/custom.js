jQuery(document).ready(function (className_function) {
    // click on next button
    jQuery('.form-wizard-next-btn').click(function (className_function) {
        const parentFieldset = jQuery(this).parents('.wizard-fieldset');
        let currentActiveStep;
        currentActiveStep = jQuery(this).parents('.form-wizard').find('.form-wizard-steps .active');
        const next = jQuery(this);
        let nextWizardStep = true;
        parentFieldset.find(`.wizard-required`).each(function () {
            const thisValue = jQuery(this).val();

            if (thisValue === "") {
                jQuery(this).siblings(".wizard-form-error").slideDown();
                nextWizardStep = false;
            } else {
                jQuery(this).siblings(".wizard-form-error").slideUp();
            }
        });
        if( nextWizardStep) {
            next.parents('.wizard-fieldset').removeClass("show", "400");
            currentActiveStep.removeClass('active').addClass('activated').next().addClass('active', "400");
            next.parents('.wizard-fieldset').next('.wizard-fieldset').addClass(`show`, "400");
            jQuery(document).find(`.wizard-fieldset`).each(function () {
                if (jQuery(this).hasClass('show')) {
                    const formAtrr = jQuery(this).attr('data-tab-content');
                    jQuery(document).find(`.form-wizard-steps .form-wizard-step-item`).each(function () {
                        if (jQuery(this).attr('data-attr') === formAtrr) {
                            jQuery(this).addClass('active');
                            const innerWidth = jQuery(this).innerWidth();
                            const position = jQuery(this).position();
                            jQuery(document).find(`.form-wizard-step-move`).css({
                                "left": position.left,
                                "width": innerWidth
                            });
                        } else {
                            jQuery(this).removeClass('active');
                        }
                    });
                }
            });
        }
    });
    //click on previous button
    jQuery('.form-wizard-previous-btn').click(function() {
        const counter = parseInt(jQuery(".wizard-counter").text());;
        const prev = jQuery(this);
        const currentActiveStep = jQuery(this).parents('.form-wizard').find('.form-wizard-steps .active');
        prev.parents('.wizard-fieldset').removeClass("show","400");
        prev.parents('.wizard-fieldset').prev('.wizard-fieldset').addClass("show","400");
        currentActiveStep.removeClass('active').prev().removeClass('activated').addClass('active',"400");
        jQuery(document).find('.wizard-fieldset').each(function(){
            if(jQuery(this).hasClass('show')){
                const formAtrr = jQuery(this).attr('data-tab-content');
                jQuery(document).find('.form-wizard-steps .form-wizard-step-item').each(function(){
                    if(jQuery(this).attr('data-attr') === formAtrr){
                        jQuery(this).addClass('active');
                        const innerWidth = jQuery(this).innerWidth();
                        const position = jQuery(this).position();
                        jQuery(document).find('.form-wizard-step-move').css({"left": position.left, "width": innerWidth});
                    }else{
                        jQuery(this).removeClass('active');
                    }
                });
            }
        });
    });
    //click on form submit button
    jQuery(document).on("click",".form-wizard .form-wizard-submit" , function(){
        const parentFieldset = jQuery(this).parents('.wizard-fieldset');
        const currentActiveStep = jQuery(this).parents('.form-wizard').find(`.form-wizard-steps .active`);
        parentFieldset.find(`.wizard-required`).each(function () {
            const thisValue = jQuery(this).val();
            if (thisValue === "") {
                jQuery(this).siblings(".wizard-form-error").slideDown();
            } else {
                jQuery(this).siblings(".wizard-form-error").slideUp();
            }
        });
    });
    // focus on input field check empty or not
    jQuery(".form-control").on('focus', function(){
        const tmpThis = jQuery(this).val();
        if(tmpThis === '' ) {
            jQuery(this).parent().addClass("focus-input");
        }
        else if(tmpThis !=='' ){
            jQuery(this).parent().addClass("focus-input");
        }
    }).on('blur', function(){
        const tmpThis = jQuery(this).val();
        if(tmpThis === '' ) {
            jQuery(this).parent().removeClass("focus-input");
            jQuery(this).siblings('.wizard-form-error').slideDown("3000");
        }
        else if(tmpThis !=='' ){
            jQuery(this).parent().addClass("focus-input");
            jQuery(this).siblings('.wizard-form-error').slideUp("3000");
        }
    });
});
$(function()
{
    $(document).on('click', '.btn-add-owner', function(e)
    {
        e.preventDefault();
        let controlForm = $('#repeatingOwners:first'),
            currentEntry = $(this).parents('.entry:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);
        newEntry.find('input').val('');
        newEntry.find('textarea').val('');
        controlForm.find('.entry:not(:last) .btn-add-owner')
            .removeClass('btn-add-owner').addClass('btn-remove-owner')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="fa fa-minus"></span>');
    }).on('click', '.btn-remove-owner', function(e)
    {
        e.preventDefault();
        $(this).parents('.entry:first').remove();
        return false;
    });

    $(document).on('click', '.btn-add-competency', function(e)
    {
        e.preventDefault();
        const controlForm = $('#repeatingCompetency:first'),
            currentEntry = $(this).parents('.entry:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);
        newEntry.find('input').val('');
        newEntry.find('textarea').val('');
        controlForm.find('.entry:not(:last) .btn-add-competency')
            .removeClass('btn-add-competency').addClass('btn-remove-competency')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="fa fa-minus"></span>');
    }).on('click', '.btn-remove-competency', function(e)
    {
        e.preventDefault();
        $(this).parents('.entry:first').remove();
        return false;
    });

    $(document).on('click', '.btn-add-brand', function(e)
    {
        e.preventDefault();
        let controlForm = $('#repeatingBrands:first'),
            currentEntry = $(this).parents('.entry:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);
        newEntry.find('input').val('');
        newEntry.find('textarea').val('');
        controlForm.find('.entry:not(:last) .btn-add-brand')
            .removeClass('btn-add-brand').addClass('btn-remove-brand')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="fa fa-minus"></span>');
    }).on('click', '.btn-remove-brand', function(e)
    {
        e.preventDefault();
        $(this).parents('.entry:first').remove();
        return false;
    });
    $(document).on('click', '.btn-add-mat', function(e)
    {
        e.preventDefault();
        let controlForm = $('#repeatingMaterials:first'),
            currentEntry = $(this).parents('.entry:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);
        newEntry.find('input').val('');
        newEntry.find('textarea').val('');
        controlForm.find('.entry:not(:last) .btn-add-mat')
            .removeClass('btn-add-mat').addClass('btn-remove-mat')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="fa fa-minus"></span>');
    }).on('click', '.btn-remove-mat', function(e)
    {
        e.preventDefault();
        $(this).parents('.entry:first').remove();
        return false;
    });

    $(document).on('click', '.btn-add-man', function(e)
    {
        e.preventDefault();
        let controlForm = $('#repeatingManufacturing:first'),
            currentEntry = $(this).parents('.entry:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);
        newEntry.find('input').val('');
        newEntry.find('textarea').val('');
        controlForm.find('.entry:not(:last) .btn-add-man')
            .removeClass('btn-add-man').addClass('btn-remove-man')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="fa fa-minus"></span>');
    }).on('click', '.btn-remove-man', function(e)
    {
        e.preventDefault();
        $(this).parents('.entry:first').remove();
        return false;
    });

    $(document).on('click', '.btn-add-mac', function(e)
    {
        e.preventDefault();
        var controlForm = $('#repeatingMachinery:first'),
            currentEntry = $(this).parents('.entry:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);
        newEntry.find('input').val('');
        newEntry.find('textarea').val('');
        controlForm.find('.entry:not(:last) .btn-add-mac')
            .removeClass('btn-add-mac').addClass('btn-remove-mac')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="fa fa-minus"></span>');
    }).on('click', '.btn-remove-mac', function(e)
    {
        e.preventDefault();
        $(this).parents('.entry:first').remove();
        return false;
    });
});


