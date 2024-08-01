const calcRem = size => `${size / 16}rem`;

const fontSizes = {
  // ! 15px은 16px or 14px 선택, 13px은 12px로 바꿔쓰세요.
  xs: calcRem(12),
  small: calcRem(14),
  base: calcRem(16),
  lg: calcRem(18),
  xl: calcRem(20),
  xxl: calcRem(24),
  titleSize: calcRem(32),
};

const fontWeights = {
  thin: '200',
  light: '300',
  regular: '400',
  bold: '500',
  semiBold: '600',
  extraBold: '700',
};

const paddings = {
  xs: calcRem(4),
  small: calcRem(8),
  base: calcRem(12),
  large: calcRem(16),
  xl: calcRem(20),
  xxl: calcRem(24),
  xxxl: calcRem(40),
};

const margins = {
  xs: calcRem(4),
  small: calcRem(8),
  base: calcRem(12),
  large: calcRem(16),
  xl: calcRem(20),
  xxl: calcRem(24),
  xxxl: calcRem(40),
};

const colors = {
  green: '#41b979',
  red: '#ef6253',
  black: '#222222',
  darkGray: '#4D4D4D',
  gray: '#8E8E8E',
  lightGray: '#f4f4f4',
  disabled: '#ebebeb',
  white: '#ffffff',
};

const borders = {
  gray: '1px solid #d3d3d3',
  lightGray: '1px solid #ebebeb',
};

const flex = {
  flexBox: (direction = 'row', align = 'center', justify = 'center') => `
    display: flex;
    flex-direction: ${direction};
    align-items: ${align};
    justify-content: ${justify};
  `,
};

const theme = {
  fontSizes,
  fontWeights,
  paddings,
  margins,
  colors,
  borders,
  flex,
};

export default theme;
