from PIL import Image
from pylab import *

im1 = array(Image.open('/Users/watsonyang/Desktop/GithubProjetcs/ImageHandle/images/linearConverged.jpg').convert('L'))


figure()
hist(im1.flatten(),128)

show()
