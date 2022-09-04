import {animate, group, query, style, transition, trigger} from '@angular/animations';

export const fader = trigger('routeAnimations', [
  transition('* <=> *', [
    query(':enter, :leave', [
      style({
        position: 'absolute',
        left: 0,
        width: '100%',
        opacity: 0,
        transform: 'scale(1) translateY(-100%)',
      }),
    ]),
    query(':enter', [
      animate('500ms ease',
        style({
          opacity: 1,
          transform: 'scale(1) translateY(0)'
        })
        )
    ])
  ]), // transition determines how to apply styles from one animation to the next
]);


function slideTo(direction: any): any {
  const optional = {optional: true};
  return [
    query(':enter, :leave', [
      style({
        position: 'absolute',
        top: 0,
        [direction]: 0,
        width: '100%'
      })
    ], optional),
    query(':enter', [
      style({ [direction]: '100%'})
    ]),
    group([
      query(':leave', [
        animate('5000ms ease',
          style({[direction]: '100%'}))
      ], optional),
      query(':enter', [
        animate('5000ms ease',
          style({[direction]: '0%'}))
      ])
    ]),
    // Normalize the page style... Might not be necessary

    // Required only if you have child animations on the page
    // query(':leave', animateChild()),
    // query(':enter', animateChild()),
  ];
}

export const slider = trigger('routeAnimations', [
  transition('enterRight => enterLeft', slideTo('left')),
  transition('enterLeft => enterRight', slideTo('right')),
  transition('enterRight => enterLeft', slideTo('left')),
  transition('enterLeft => enterRight', slideTo('right')),
  // export const slider = trigger('routeAnimations', [
  // transition('* <=> isLeft', slideTo('left')),
  // transition('* <=> isRight', slideTo('right')),
  // transition('isRight <=> *', slideTo('left')),
  // transition('isLeft <=> *', slideTo('right')), // transition determines how to apply styles from one animation to the next
]);
