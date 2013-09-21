Pose
====
The Animated Poseur
Software Requirements Specification
Author: Burak Firik
Abstract: This document discusses and elaborates on the requirements for an Animated Sprite 
Editor, an application that will develop animated sprites which can be used for game 
engines.The interface, along with the functionality of the sprite editor will be discussed in 
this program.
Based on IEEE Std 830TM-1998 (R2009) document format
11 Introduction
There is a growing demand for software that can create animated sprite for game developers. With this 
software, we will try to fulfill that demand by developing software that can create sprites in various 
states. Once a sprite state is created, it can later be exported to a game engine, where it can serve as a 
game character with multiple functionalities.
1.1 Purpose
The core reason behind this document is to elaborate as to how The Animated Poseur, an animated sprite 
editor, is going to function and serve the purpose of game developers looking for an application that can 
swiftly, intelligently, and comfortably, develop sprites with various states. Our intended audience is 
game developers, who wish to created sprites, with various states, and export them to their game 
engines. Our sprites data will be stored in an XML, which makes it easy to export. The audience is not 
just limited to game developers, we also expect to have an audience of users who are designers and 
might wish to create sprites for various other reasons besides game development. This document will 
serve as a contract amidst all the members on the team as to how the software will be constructed. Upon 
reading this document, one should easily be able to understand as to how the animated sprite editor 
application is going to function, how it visually looks like.
1.2 Scope
The Animated Poseur will serve as a professional tool for Game developers looking to create and edit 
sprite poses and animation states for them. This application will offer an intuitive graphical user 
interface, which can assist the developer in creating states and sprite poses of his desire. Once the user 
has constructed his state for his sprite type, our application will offer him a preview of his state in 
action. Our user will also have to ability to alter the sprite poses of his sprite state if he is not satisfied, 
and later export the complete animated sprite state to his game engine once he is done constructing it. 
We expect our users to use this application as a one stop for all, since he will be able to create the 
individual poses, and later have them construct a state of his choice. For example, the user can create a 
running state, with various poses. Given the flexibility of our application, we expect tremendous global 
demand and the scope reaching beyond just ordinary game developers.
1.3 Definitions, acronyms, and abbreviations
Sprite – A 2D image or animation. They are generally used as game characters; think of Mario in Super
Mario Bros, he was a sprite with various sprite animation states.
Sprite Pose – A Sprite pose is an image of a sprite, which can be rendered on the screen for a given 
time. Our application also possesses the ability to create sprite poses.
2Sprite Type – We can have different kind of Sprites that physically look differently, and behave 
differently based on their states. For example, Mario and Koopa are both sprites, but the type is 
different.
Sprite Animation State – A sprite can have various states, for example, there could be state where he is 
jumping, a state where he is dancing, and a state where he is running. Our application will give the user 
the strategic ability of creating animated sprite states of his choice for his designed sprite.
Pose List – A pose list is a list of sprite poses that is used to create an animation state. Each animation 
state will refer to it’s own pose list when it comes time for rendering.
XML –XML which stands for Extensible Markup Language, is the language we will use to store and 
load our data.
XSD –The schema file, which will be used for the validation of our XML.
Rendering- The process of generating an image.
Frame rate – The number of times a pose is rendered each second
IEEE – Institute of Electrical and Electronics Engineers, the “world’s largest professional association 
for the advancement of technology”.
Framework – In an object-oriented language, a collection of classes and interfaces that collectively 
provide a service for building applications or additional frameworks all with a common need.
GUI – Graphical User Interface, visual controls like buttons inside a window in a software application 
that collectively allow the user to operate the program.
Java – A high-level programming language that uses a virtual machine layer between the Java 
application and the hardware to provide program portability.
UML – Unified Modeling Language, a standard set of document formats for designing software 
graphically.
Use Case Diagram – A UML document format that specifies how a user will interact with a system. 
Note that these diagrams do not include technical details. Instead, they are fed as input into the design 
stage (stage after this one) where the appropriate software designs are constructed based in part on the 
Use Cases specified in the SRS.
31.4 References
IEEE Std 830TM-1998 (R2009) – IEEE Recommended Practice for Software Requirements
Specification
1.5 Overview
This document will define how the Animated Poseur application will look and operate, along with how 
to actually use and take advantage of the advanced capabilities of our application. This document is 
Java-free, and will not explain how to build the actual application. Instead, this document will simply 
serve as a guideline, which outlines what to build, not how to build it. In section 2 of this text, we will 
instead invest our time in explaining the design of our application. Section 3 will define how the user 
interface will be laid out, along with an elaboration of the tools that exist in our animated sprite editor.
Section 4 will possess our Table of Context, an Index, and References.
42 Overall description
Game developers have been long complaining that they wish to have an easy way of creating sprite 
types, with various states, for their game engines. Presently, an efficient and affordable solution does not 
exist; hence developers invest a lot of time creating animated sprites with inefficient technologies. Our 
application sets to fill in the void, and will make the life of the game developer tremendously easier. Our 
application will reduce the time a developer generally spends in designing multiple sprites with various 
animation states. The application will give the developer the ability to edit the sprite, create custom 
states for a sprite, and seamlessly integrate what they just developed into a game engine using XML 
technology.
2.1 Product perspective
Our product is independent, and totally self-contained. We expect our product to not be complicated. We 
feel that the last thing a game developer, or any potential user, wants is an incomprehensible GUI that is 
hard to navigate and use. No matter how efficient the application is, if it’s not friendly and easy to use, a 
developer might not use it. With our application, we will focus on making it as easy as possible, and 
neatly integrating both sprite pose editing, and animation state rendering. Game developers generally 
have strict deadlines to meet, so saving their time is one of our products major perspectives. Our 
application will give the user to create a sprite types using Ellipses, lines, Rectangles. The user can also 
set the outline thickness, move various components to his desired location on the canvas, and choose the 
color that he desires for his pose, etc. The application will give the user the ability to create an animation 
state with his given set of poses, and the ability to render his animation sprite. Our product will also 
allow the user to edit previous animation states, and load previously created sprites for future changes. A 
major perspective of our product is the ability to export the animation state to game engines, such a 
feature save game developers thousands of hours per year.
2.1.1 System Interface
Our system consists of five major states: create, select, edit, render, complete.
Figure 2.1: States our application will enter based on the user action.
Animated Poseur System Interface States
Create Select Edit Render Complete
5In the create state our application is about to create a new still pose that can eventually contribute to our 
animated state. We will provide a full selection tools for creating various objects, such as lines, ellipses, 
etc. The select state is when a pose is selected from the canvas, or from the Pose list. This mode will set 
the gateway for editing capabilities. In the edit state, the selection whether it hails from the pose on the 
canvas, or from the pose list, can be fully edited. You can change the color, add more shapes, and then 
later store it. Our application enters the render state once a full set of poses in a state is created. The 
render state will allow our application to give us a live run on how to sprite in that given state will 
behave. The complete state is when you have created your animated sprite state, or pose. These states 
will allow us to fully control our application smoothly.
2.1.2 User Interfaces:
Our user interface for our product will have the ability to edit a newly created sprite, using various 
editing tools. You can add rectangles, ellipses, lines, change the pose outline thickness, its color, etc. In 
addition to pose editing in our application, you will also be able to create your own animation state with 
the very same poses that you’ve constructed for your sprite. Once you’ve formed an animation state, our 
interface will allow you to render the sprite state for a live play of how your sprite, with its animation 
state, will run in your game engine. Our interface will also allow you to edit, rename, or duplicate an 
already created animation state. For example, suppose you have created a state called ‘Jogging’, and 
wish to edit a pose in the Jogging, you can easily click on the pose in the list and instantly begin editing 
it. Our application will display all the poses for a given animation state since it’s important to show the 
user what kind of poses the animation state comprises of.
Due to the sophistication of the application, users will use the application in limited ways. For example, 
if you open a new sprite, the user will have restrictions in selecting a shape, since there is no shape 
created in the canvas yet. We feel that our user interface should be as smart as possible; we don’t want 
to confuse our users by enabling all the tools, which aren’t even necessary.
As far as the actual use of a typical user is concerned, we expect a user to first create a new sprite, and 
then create a new animation state with a specific name. For that given animation state, we expect a user 
to take advantage of our Pose editing capabilities, such that he can design all the poses necessary for his 
sprite state by using our application. Once he has designed all the poses for a state, he can render his 
sprite’s newly created animation state to get a feel as to how it will look in the actual game. If he is not 
satisfied, he will have a visual representation of the pose list, therefore he can click on the pose he 
wishes to edit, and instantly begin editing it to suit his needs. Our application will also give the user an 
easy intuitive way of saving his sprite with his states. That way, if in the future he needs to load a sprite 
with his states, he will just be a few clicks away from it. Below is a typical use case of a user using our 
application.
6Figure 2.2: Use-Case diagram of Animated Poseur Overview:
7Sample Use-Case diagrams: The Animated Poseur
Once the user begins to use our application, there will be different kind of things we expect our user to 
do. He can create a new sprite, with various animation states, edit an existing animation state of a sprite, 
or edit a pose of animation state. We have below various major use cases of our programs that elaborate 
as to how the user will communicate with our application.
Figure 2.3 – Filling a shape with a color
Figure 2.4: Save As functionality for a Pose
8Figure 2.5: Overview of Exit button
Figure 2.6: Renaming a state
Figure 2.7: Creating a new animation state
Figure 2.8: Loading a pose
910
Figure 2.9: Editing an Pose
Figure 3.0: Reordering a pose
Figure 3.1: Rendering a an animation state11
Figure 3.2: changing duration time of a pose Figure 3.3: Deleting an animation state
Figure 3.3: Deleting a pose Figure 3.4: Duplicate state2.1.3 Hardware Interfaces
The application should be designed and constructed such that it may be easily ported and published on 
multiple platforms. Target platforms should include the Windows PC, Mac, and Linux – Since game 
developers are very active in those aforementioned platforms. Based on the response of our product, we 
will also consider lite mobile applications for the Android and iOS platform.
2.1.4 Software Interfaces
Our product will be developed in the Java language, using version 7.0. We will thoroughly debug our 
program on the major platforms we plan to support, just in case there is a cross platform conflict. Since 
we know that Java is pretty good with cross platform compatibility, we will primarily use Java, and 
based up on the success of our application, we might port it over to iOS, using Objective-C. For the first 
few months of our product deployment, we will strictly focus on offering an unparalleled software 
experience on desktops running our supported operating systems.
2.1.5 Communications Interfaces
This application does not require any access to the Internet, since it’s an offline application. There will 
be no networking requirements.
2.1.6 Memory Constraints
Since our product will be dealing with many images, we will specifically focus on ensuring that our 
application does not run out of memory when rendering the animation state, or loading a sprite, etc. The 
memory requirements for each platform will be different, but that memory budgets should be defined 
carefully before our implementation.
2.1.7 Operations
The new sprite operation in our program creates a sprite with various animation states. These animation 
states are constructed by creating a series of Poses (still images, designed in our canvas). Our application 
possesses the ability to create, and edit poses for a specific animation state. One of the major operations 
in our product is the ability to render the animation state, once you have created an animation state. A 
core operation would also be viewing the pose list, which is the essentially the list if poses that 
constructs the animation state, and having the ability to alter specific pose if you are not satisfied with 
the outcome of the animation state.
Another major operation is loading a sprite, as soon as you search in your computer for a sprite to load, 
and successfully load it, all the animation states corresponding with that sprite are going to be loaded as
12well (thanks to XML). What this means is, everything will be neatly organized in the program. Each 
sprite, once loaded, has his respective states in the right panel under animation states. For example, if 
you’ve exported a sprite to your game engine, and feel like the way the sprite is functioning doesn’t 
meet your needs, you can easily use the load operation in our product, and manually edit that animation 
state to suit your needs. We feel like this load feature will make the lives of game developers a lot easier, 
given it’s flexibility in editing poses, and how easy it is to save or save as your work.
2.1.8 Site Adaptation Requirements
N/A
2.2 Product functions
The primary function of this product is to make the lives of game developers a lot easier. Based on our 
research, we’ve concluded that game developers invest a lot of time designing sprites, which can often 
increase the cost of their development project. Our application seeks to bridge the gap, and will offer 
them the functionality of instantly designing a sprite, with it’s designed states. Another important 
functionality of our product is the user-friendly way of exporting their sprite to their game engine. It’s a 
no hassle process, where editing the created sprite for future changes is just as intuitive and easy.
2.3 User characteristics
While our application can also be used by general users who wish to create animated sprites for fun, the 
primary user base that we expect are game developers that wish to create animated sprites for their 
product. Given how flexible and easy to use our program is, we expect a huge demand by the gaming 
community.
2.4 Constraints
N/A
2.5 Assumptions and dependencies
We assume that the user understands how has his game engine works, since we won’t be describing the 
steps needed to port his application over to his game engine. This application depends on a computer, 
which has enough RAM and file storage to store all the pose files you will be creating for the animation 
state of the sprite.
2.6 Apportioning of the Requirements
It is possible that in the future we add support for undo, and redo, along with deploying our product for 
the iOS and Android mobile operating systems. For now, we feel that efficiency is our main goal. We
13might also add a feature where can have your sprite converted to various file extension formats, aside 
from XML, and. POSE. We will notify our users once future updates are performed.
3 Specific requirements
Animated Poseur will use a simple interface with controls for each respective segment of the 
application. The animation state renderer will have it’s own contextual buttons. The reason for this is 
because we don’t want our user to swim through a multitude of different buttons, therefore we 
emphasize on making available what the user will be using at a given time. So if for example, the user is 
editing a pose, we don’t feel like showing him that he can duplicate his state, even though that option is 
available towards the left side of the panel. Our GUI is very straightforward, and incredibly userfriendly. One of the major requirements we had was making our application was friendly as possible 
hence our GUI controls are neatly drawn, carefully spaced, and properly aligned.
3.1 External interfaces and product Functionality elaboration:
The Figure below shows how the GUI would be laid out in our design stage. Note that each portion of 
the application has it’s own contextual buttons for product easability reasons
Figure 3.1: Animated Poseur GUI while pose editing is in progress from a selected animation state
14
