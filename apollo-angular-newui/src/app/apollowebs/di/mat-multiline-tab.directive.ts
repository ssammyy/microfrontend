import { Directive, OnInit, OnDestroy, HostBinding, ElementRef } from '@angular/core';
import { Subject } from 'rxjs';
import { MatTabGroup } from '@angular/material/tabs';
import { takeUntil } from 'rxjs/operators';

@Directive({
    selector: '[appMatMultilineTab]'
})
export class MatMultilineTabDirective implements OnInit, OnDestroy {
    @HostBinding('class.mat-tab-multiline') class = true;

    private readonly onDestroy = new Subject();
    // private readonly resizeObserver: ResizeObserver;
    private resizeTimeout: number;

    constructor(
        private elementRef: ElementRef,
        private matTabGroup: MatTabGroup
    ) {
        this.matTabGroup.selectedTabChange.pipe(
            takeUntil(this.onDestroy)
        ).subscribe(() => this.updateInkBarPosition());

        // this.resizeObserver = new ResizeObserver(() => {
        //     if (this.resizeTimeout) {
        //         clearTimeout(this.resizeTimeout);
        //     }
        //     this.resizeTimeout = setTimeout(() => this.updateInkBarPosition(), 100);
        // });
    }

    ngOnInit() {
        // this.resizeObserver.observe(
        //     this.elementRef.nativeElement.querySelector('.mat-tab-header:first-child')
        // );
    }

    ngOnDestroy() {
        // tslint:disable-next-line:no-unused-expression
        // this.resizeObserver && this.resizeObserver.disconnect();

        this.onDestroy.next();
        this.onDestroy.complete();
    }

    private updateInkBarPosition() {
        const headerElement: HTMLElement = this.elementRef.nativeElement.querySelector(
            '.mat-tab-header:first-child'
        );
        const activeTabElement: HTMLElement = headerElement.querySelector(
            '.mat-tab-label-active'
        );
        const inkBarElement: HTMLElement = headerElement.querySelector('.mat-ink-bar');
        const listElement: HTMLElement = headerElement.querySelector('.mat-tab-list');
        const bottom = listElement.offsetHeight -
            (activeTabElement.offsetTop + activeTabElement.offsetHeight);

        inkBarElement.style.bottom = bottom + 'px';
        inkBarElement.style.left = activeTabElement.offsetLeft + 'px';
    }
}
